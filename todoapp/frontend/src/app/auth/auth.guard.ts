import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

/**
 * Functional route guard that protects routes requiring authentication.
 *
 * How it works:
 * 1. Checks if the user is currently authenticated via AuthService.
 * 2. If authenticated, allows navigation by returning `true`.
 * 3. If not authenticated, redirects the user to the /login page
 *    by returning a UrlTree (Angular's way of declaratively redirecting).
 *
 * Usage: attach to a route's `canActivate` array in the route config.
 */
export const authGuard: CanActivateFn = () => {
  // Inject dependencies — AuthService for auth state, Router for redirects
  const authService = inject(AuthService);
  const router = inject(Router);

  // Check if the user has a valid session/token
  if (authService.isAuthenticated()) {
    // User is logged in — allow access to the route
    return true;
  }

  // User is NOT authenticated — redirect to the login page.
  // Returning a UrlTree tells the router to navigate there instead.
  return router.createUrlTree(['/login']);
};