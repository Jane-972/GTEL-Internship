package org.jane.gtelinternship.product.domain.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jane.gtelinternship.product.domain.model.FullProduct;
import org.jane.gtelinternship.product.domain.model.LogicomProduct;
import org.jane.gtelinternship.product.domain.model.WooProduct;
import org.jane.gtelinternship.product.infra.client.woo.WooClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCacheService {
  private final LogicomService logicomService;
  private final WooClient wooClient;

  // Cache for both product types
  private volatile List<FullProduct<LogicomProduct>> logicomProductsCache = new ArrayList<>();
  private volatile List<FullProduct<WooProduct>> wooProductsCache = new ArrayList<>();
  private volatile ConcurrentHashMap<String, FullProduct<LogicomProduct>> logicomBySkuCache = new ConcurrentHashMap<>();
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

  public List<FullProduct<WooProduct>> getAllWooProducts() {
    log.debug("Serving {} WooCommerce products from cache", wooProductsCache.size());
    return new ArrayList<>(wooProductsCache);
  }

  // Manual refresh for when we know data changed
  public CompletableFuture<Void> forceRefresh() {
    log.info("Manual refresh requested");
    return CompletableFuture.runAsync(this::refreshAllProducts);
  }
  public List<FullProduct<LogicomProduct>> getAllProducts() {
    log.debug("Serving {} products from cache", logicomProductsCache.size());
    return new ArrayList<>(logicomProductsCache); // Return copy for safety
  }

  public Optional<FullProduct<LogicomProduct>> getProductBySku(String sku) {
    FullProduct<LogicomProduct> cached = logicomBySkuCache.get(sku);
    if (cached != null) {
      log.debug("Serving product {} from cache", sku);
      return Optional.of(cached);
    }
    return Optional.empty();
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

      // Refresh Logicom products
      List<FullProduct<LogicomProduct>> freshLogicomProducts =
        logicomService.getAllProductsFull().toList();

      // Refresh WooCommerce products
      List<FullProduct<WooProduct>> freshWooProducts =
        wooClient.getAllProductsFull();

      // Atomically update both caches
      logicomProductsCache = freshLogicomProducts;
      wooProductsCache = freshWooProducts;

      // Also update the SKU-based cache for Logicom
      logicomBySkuCache.clear();
      freshLogicomProducts.forEach(product ->
        logicomBySkuCache.put(product.product().getSku(), product));

      lastRefresh = LocalDateTime.now();

      log.info("Completed refresh of {} Logicom + {} WooCommerce products",
        freshLogicomProducts.size(), freshWooProducts.size());

    } catch (Exception e) {
      log.error("Failed to refresh products cache", e);
    } finally {
      isRefreshing = false;
    }
  }

  // Health check to see cache status
  public CacheStatus getCacheStatus() {
    return new CacheStatus(
      logicomProductsCache.size(),
      wooProductsCache.size(),
      lastRefresh,
      isRefreshing
    );
  }

  public record CacheStatus(
    int logicomProductCount,
    int wooProductCount,
    LocalDateTime lastRefresh,
    boolean isRefreshing
  ) {}
}