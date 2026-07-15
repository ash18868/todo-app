import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

/**
 * Functional HTTP interceptor that attaches a Bearer token to outgoing requests.
 *
 * How it works:
 * 1. Reads the current auth token from AuthService (a signal).
 * 2. If a token exists, clones the request and adds an Authorization header.
 * 3. If no token is available, forwards the original request unchanged.
 *
 * Cloning is necessary because Angular HttpRequest objects are immutable —
 * you cannot modify them directly.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Inject AuthService to access the stored JWT token
  const authService = inject(AuthService);

  // Read the current token value from the signal
  const token = authService.token();

  if (token) {
    // Clone the request and attach the Authorization header with a Bearer token
    const clonedReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });

    // Forward the modified request to the next handler in the chain
    return next(clonedReq);
  }

  // No token available — forward the original request as-is
  return next(req);
};