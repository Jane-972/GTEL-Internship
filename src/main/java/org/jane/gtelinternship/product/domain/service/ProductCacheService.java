package org.jane.gtelinternship.product.domain.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.LogicomProduct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCacheService {
  private final LogicomService logicomService;

  // Storing all products in memory
  private volatile List<FullProduct<LogicomProduct>> allProductsCache = new ArrayList<>();
  private volatile ConcurrentHashMap<String, FullProduct<LogicomProduct>> productBySkuCache = new ConcurrentHashMap<>();
  private volatile LocalDateTime lastRefresh = LocalDateTime.MIN;
  private volatile boolean isRefreshing = false;

  @PostConstruct
  public void initializeCache() {
    log.info("Initializing product cache on startup...");
    // Load cache immediately on startup (async so startup isn't blocked)
    CompletableFuture.runAsync(this::refreshAllProducts);
  }

  // Auto-refresh every 12 hours
  @Scheduled(fixedRate = 12, timeUnit = TimeUnit.HOURS)
  public void scheduledRefresh() {
    log.info("Scheduled refresh triggered");
    refreshAllProducts();
  }

  // Manual refresh for when we know data changed
  public CompletableFuture<Void> forceRefresh() {
    log.info("Manual refresh requested");
    return CompletableFuture.runAsync(this::refreshAllProducts);
  }
  public List<FullProduct<LogicomProduct>> getAllProducts() {
    log.debug("Serving {} products from cache", allProductsCache.size());
    return new ArrayList<>(allProductsCache); // Return copy for safety
  }

  public Optional<FullProduct<LogicomProduct>> getProductBySku(String sku) {
    return Optional.ofNullable(productBySkuCache.get(sku));
  }


  // This is where the long operation will happen (in background)
  private void refreshAllProducts() {
    if (isRefreshing) {
      log.info("Refresh already in progress, skipping");
      return;
    }

    try {
      isRefreshing = true;
      log.info("Starting background refresh of all products...");

      // This is the expensive call
      List<FullProduct<LogicomProduct>> freshProducts =
        logicomService.getAllProductsFull().toList();

      // Atomically update the cache
      allProductsCache = freshProducts;

      // Also update the SKU-based cache
      productBySkuCache.clear();
      freshProducts.forEach(product ->
        productBySkuCache.put(product.product().getSku(), product));

      lastRefresh = LocalDateTime.now();

      log.info("Completed refresh of {} products", freshProducts.size());

    } catch (Exception e) {
      log.error("Failed to refresh products cache", e);
    } finally {
      isRefreshing = false;
    }
  }

  // Returns a stream over all products (from the List cache)
  public Stream<FullProduct<LogicomProduct>> streamAllProducts() {
    return allProductsCache.stream();
  }


  // Health check to see cache status
  public CacheStatus getCacheStatus() {
    return new CacheStatus(
      allProductsCache.size(),
      lastRefresh,
      isRefreshing
    );
  }

  public record CacheStatus(
    int productCount,
    LocalDateTime lastRefresh,
    boolean isRefreshing
  ) {}
}