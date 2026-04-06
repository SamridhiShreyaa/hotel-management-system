package com.pesu.hotel.auth.pattern;

/**
 * Repository Pattern (Framework-Enforced)
 *
 * In this project, Spring Data JPA repository interfaces inside
 * com.pesu.hotel.repository act as repository abstractions.
 *
 * Why this pattern is used in auth module:
 * - Keeps controller/service logic independent of low-level data access code.
 * - Supports clear separation of concerns in MVC architecture.
 * - Reduces boilerplate via framework-generated repository implementations.
 */
public final class RepositoryPatternNotes {

	private RepositoryPatternNotes() {
	}
}
