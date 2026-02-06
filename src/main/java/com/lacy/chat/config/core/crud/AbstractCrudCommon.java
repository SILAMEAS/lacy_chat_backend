package com.lacy.chat.config.core.crud;


import com.lacy.chat.share.exception.NotFoundException;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.FluentQuery;

import java.util.Locale;
import java.util.Optional;

/**
 * Abstract class for CRUD common service.
 *
 * @param <E> refers to the entity type
 * @param <T> refers to the type of 'primary key'
 * @author Chanthorn LY
 * @since 08-February-2024
 */
public abstract class AbstractCrudCommon<
        M, E, T, R extends JpaRepository<E, T> & JpaSpecificationExecutor<E>>
    implements GetClassType {

  private static final String ENTITY_NOT_FOUND = "Entity with id: %s not found";
  private static final String NOT_FOUND = "Entity not found";

  protected final R baseRepository;
  protected final M mapper;

  protected AbstractCrudCommon(R baseRepository, M mapper) {
    this.baseRepository = baseRepository;
    this.mapper = mapper;
  }

  /**
   * Creates a Pageable object for pagination with the given page number and page size.
   *
   * @param pageNumber the number of the page to retrieve (0-indexed)
   * @param pageSize the maximum number of items to retrieve per page
   * @return a Pageable object representing the pagination settings
   */
  protected Pageable toPageable(int pageNumber, int pageSize) {
    return PageRequest.of(pageNumber - 1, pageSize);
  }

  /**
   * Creates a Pageable object for pagination with the given page number, page size, sort by field,
   * and sort direction.
   *
   * @param pageNumber the number of the page to retrieve (0-indexed)
   * @param pageSize the maximum number of items to retrieve per page
   * @param sortByField the field to sort the results by
   * @param sortDirection the direction to sort the results in (either "ASC" or "DESC")
   * @return a Pageable object representing the pagination settings
   */
  protected Pageable toPageable(
      int pageNumber, int pageSize, @NonNull String sortByField, @NonNull String sortDirection) {
    final var sort =
        Sort.by(Sort.Direction.valueOf(sortDirection.toUpperCase(Locale.ENGLISH)), sortByField);
    return PageRequest.of(pageNumber - 1, pageSize, sort);
  }

  protected E findById(@NonNull T id) {
    return this.baseRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(String.format(ENTITY_NOT_FOUND, id)));
  }

  protected E save(@NonNull E entity) {
    return this.baseRepository.save(entity);
  }

  protected E update(@NonNull E entity) {
    return this.baseRepository.save(entity);
  }

  protected void deleteById(@NonNull T id) {
    this.baseRepository.deleteById(id);
  }

  /**
   * Finds a single entity matching the given query.
   *
   * @param query the fluent query to execute
   * @return the matched entity
   * @throws NotFoundException if no entity is found
   */
  protected E findOne(FluentQuery.FetchableFluentQuery<E> query) {
    return query.one().orElseThrow(() -> new NotFoundException(NOT_FOUND));
  }

  /**
   * Finds a single entity matching the given query.
   *
   * @param query the fluent query to execute
   * @return the optional entity
   */
  protected Optional<E> findOneOptional(FluentQuery.FetchableFluentQuery<E> query) {
    return query.one();
  }

  /**
   * Retrieves a paginated (and optionally sorted) list of entities of type {@code E}.
   *
   * <p>If a {@link Specification} is provided, it will be applied to filter the results. Otherwise,
   * all entities are returned. Sorting is optional; if {@code sortByField} is null or empty, no
   * sorting is applied.
   *
   * @param pageable the page number to retrieve (1-based) {@code sortByField} is null)
   * @return a {@link Page} containing the entities for the requested page
   * @throws IllegalStateException if a {@link Specification} is provided but the repository does
   *     not support it
   */
  protected Page<E> findAll(@NonNull Pageable pageable) {

    // No Specification, return all entities
    return baseRepository.findAll(pageable);
  }

  /**
   * Retrieves a paginated (and optionally sorted) list of entities of type {@code E}.
   *
   * <p>If a {@link Specification} is provided, it will be applied to filter the results. Otherwise,
   * all entities are returned. Sorting is optional; if {@code sortByField} is null or empty, no
   * sorting is applied.
   *
   * @param spec an optional {@link Specification} to filter the results (can be null)
   * @param pageable the page number to retrieve (1-based) {@code sortByField} is null)
   * @return a {@link Page} containing the entities for the requested page
   * @throws IllegalStateException if a {@link Specification} is provided but the repository does
   *     not support it
   */
  protected Page<E> findAll(@NonNull Specification<E> spec, Pageable pageable) {
    return baseRepository.findAll(spec, pageable);
  }
}
