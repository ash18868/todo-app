import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

/**
 * Central authentication service responsible for managing user login state.
 *
 * - Stores the JWT in a signal so the rest of the app reacts to auth changes.
 * - Persists the token in localStorage so sessions survive page refreshes.
 * - Used by `authInterceptor` to attach the token to outgoing HTTP requests.
 * - Used by `authGuard` to gate access to protected routes.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  // Signal holding the current JWT (null when not authenticated).
  // Initialized from localStorage so a returning user stays logged in.
  token = signal<string | null>(this.getStoredToken());

  // Derived signal — automatically updates whenever `token` changes.
  // Components and guards can read this reactively without manual subscriptions.
  isAuthenticated = computed(() => this.token() !== null);

  /**
   * Sends credentials to the backend and returns an Observable that emits
   * the raw JWT string on success. The caller is responsible for subscribing
   * and calling `setToken()` with the result.
   */
  login(username: string, password: string) {
    return this.http.post('http://localhost:8080/login', { username, password }, { responseType: 'text' });
  }

  /** Store the token in memory and localStorage */
  setToken(token: string): void {
    this.token.set(token);
    localStorage.setItem('auth_token', token);
  }

  /** Clear auth state and redirect to login */
  logout(): void {
    this.token.set(null);
    localStorage.removeItem('auth_token');
    this.router.navigate(['/login']);
  }

  /** Retrieve token from localStorage on app startup */
  private getStoredToken(): string | null {
    return localStorage.getItem('auth_token');
  }
}