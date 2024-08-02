package com.example.demo.repository;

import com.example.demo.dto.ProductStoreDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductStoreRepositoryImpl implements ProductStoreRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ProductStoreDTO> findProductStoreInfo(Long storeId, String keyword, List<Long> categoryIds, Pageable pageable) {
        StringBuilder jpql = getStringBuilder(keyword, categoryIds);

        TypedQuery<ProductStoreDTO> query = entityManager.createQuery(jpql.toString(), ProductStoreDTO.class);
        query.setParameter("storeId", storeId);

        if (categoryIds != null && !categoryIds.isEmpty()) {
            query.setParameter("categoryIds", categoryIds);
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            String[] keywords = keyword.trim().toLowerCase().split("\\s+");
            for (int i = 0; i < keywords.length; i++) {
                query.setParameter("keyword" + i, "%" + keywords[i] + "%");
            }
        }

        int totalRows = query.getResultList().size();
        List<ProductStoreDTO> resultList = query.setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(resultList, pageable, totalRows);
    }

    @Override
    public Optional<ProductStoreDTO> findByIdAndStoreId(Long id, Long storeId) {
        String jpql = "SELECT new com.example.demo.dto.ProductStoreDTO(ps.priceId, ps.stockId, p, s, ps.salePrice, ps.stockQuantity, ps.priceCreatedAt, ps.stockCreatedAt, ps.priceUpdatedAt, ps.stockUpdatedAt) FROM ProductStore ps JOIN ps.product p JOIN ps.store s WHERE s.id = :storeId AND p.id = :productId";

        try {
            ProductStoreDTO productStoreDTO = entityManager.createQuery(jpql, ProductStoreDTO.class)
                    .setParameter("storeId", storeId)
                    .setParameter("productId", id)
                    .getSingleResult();
            return Optional.of(productStoreDTO);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private static StringBuilder getStringBuilder(String keyword, List<Long> categoryIds) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new com.example.demo.dto.ProductStoreDTO(ps.priceId, ps.stockId, p, s, ps.salePrice, ps.stockQuantity, ps.priceCreatedAt, ps.stockCreatedAt, ps.priceUpdatedAt, ps.stockUpdatedAt) ");
        jpql.append("FROM ProductStore ps ");
        jpql.append("JOIN ps.product p ");
        jpql.append("JOIN ps.store s ");
        jpql.append("WHERE s.id = :storeId ");

        if (categoryIds != null && !categoryIds.isEmpty()) {
            jpql.append("AND p.category.id IN :categoryIds ");
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            String[] keywords = keyword.trim().toLowerCase().split("\\s+");
            jpql.append("AND (");
            for (int i = 0; i < keywords.length; i++) {
                if (i > 0) {
                    jpql.append(" OR ");
                }
                jpql.append("LOWER(p.name) LIKE :keyword").append(i).append(" OR LOWER(p.description) LIKE :keyword").append(i);
            }
            jpql.append(") ");
        }
        return jpql;
    }
}
