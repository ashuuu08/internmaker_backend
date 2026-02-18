# JWT Token Mismatch Error - Bug Fix Report

## 🐛 Problem Identified

**Error:** Token mismatch error on frontend when attempting authentication

**Root Cause:** The `AuthService.java` was passing a `String` (username) to `JwtService.generateToken()` instead of a `UserDetails` object.

---

## 🔍 Technical Analysis

### Issue Location
**File:** `src/main/java/com/internmaker/internmaker_backend/service/AuthService.java`

### What Was Wrong

#### Before (Lines 33 & 42):
```java
var jwtToken = jwtService.generateToken(user.getUsername()); // ❌ WRONG
```

**Problem:** 
- `user.getUsername()` returns a `String` (the email)
- `JwtService.generateToken()` expects a `UserDetails` object
- This type mismatch caused the JWT token to be generated incorrectly
- The token validation would fail because the token subject didn't match the expected user details

### Why This Happened

The `JwtService.generateToken()` method signature is:
```java
public String generateToken(UserDetails userDetails)
```

But the code was calling it with:
```java
jwtService.generateToken(user.getUsername()) // String instead of UserDetails
```

Since the `User` entity implements `UserDetails`, we should pass the entire `user` object, not just the username string.

---

## ✅ Solution Applied

### Changes Made

**File:** `AuthService.java`

#### 1. Fixed `register()` method (Line 33):
```java
// ✅ FIXED: Pass the User object (which implements UserDetails)
var jwtToken = jwtService.generateToken(user);
```

#### 2. Fixed `login()` method (Line 42):
```java
// ✅ FIXED: Pass the User object (which implements UserDetails)
var jwtToken = jwtService.generateToken(user);
```

#### 3. Removed orphaned code (Lines 45-55):
Removed the duplicate `loadUserByUsername()` method that was incorrectly placed in `AuthService`. This method already exists in `CustomUserDetailsService` where it belongs.

### Cleaned up imports:
- Removed unused: `UserDetails`, `UsernameNotFoundException`, `ArrayList`

---

## 🔄 How JWT Token Flow Works Now

### Registration Flow:
1. User registers with email, password, name, phone, role
2. Password is encoded using BCrypt
3. User entity is saved to database
4. **JWT token is generated using the complete User object** ✅
5. Token, full name, and role are returned to frontend

### Login Flow:
1. User provides email and password
2. Spring Security authenticates the credentials
3. User entity is retrieved from database
4. **JWT token is generated using the complete User object** ✅
5. Token, full name, and role are returned to frontend

### Token Validation Flow (JwtFilter):
1. Request comes with `Authorization: Bearer <token>` header
2. Token is extracted and username (email) is extracted from token
3. UserDetails are loaded using `CustomUserDetailsService`
4. Token is validated against the UserDetails
5. If valid, user is authenticated in SecurityContext

---

## 🧪 Testing Recommendations

### 1. Test Registration
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "fullName": "Test User",
  "email": "test@example.com",
  "password": "password123",
  "phone": "1234567890",
  "role": "STUDENT"
}
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "fullName": "Test User",
  "role": "STUDENT"
}
```

### 2. Test Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "fullName": "Test User",
  "role": "STUDENT"
}
```

### 3. Test Protected Endpoint
```bash
GET http://localhost:8080/api/courses
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Expected:** Should return courses data without 401/403 error

---

## 📋 Verification Checklist

- [x] Fixed `register()` method to pass User object
- [x] Fixed `login()` method to pass User object
- [x] Removed duplicate `loadUserByUsername()` method
- [x] Cleaned up unused imports
- [ ] Compile the project (requires Maven/Java setup)
- [ ] Start the backend server
- [ ] Test registration endpoint
- [ ] Test login endpoint
- [ ] Test protected endpoints with generated token
- [ ] Verify frontend can authenticate successfully

---

## 🚀 Next Steps

1. **Compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   Or if using an IDE, run the `InternmakerBackendApplication` class

3. **Test with frontend:**
   - Clear any stored tokens in browser localStorage
   - Try registering a new user
   - Try logging in
   - Verify protected routes work with the new token

4. **Monitor logs:**
   - Check for any JWT-related exceptions
   - Verify token generation and validation logs

---

## 🔐 Security Notes

- JWT secret key is configured in `application.yml`
- Token expiration is set to 24 hours (86400000 ms)
- Tokens are signed using HS256 algorithm
- User passwords are encrypted using BCrypt
- The `User` entity properly implements `UserDetails` interface

---

## 📝 Additional Information

### Related Files:
- ✅ `AuthService.java` - **FIXED**
- ✅ `JwtService.java` - No changes needed (was already correct)
- ✅ `JwtFilter.java` - No changes needed (was already correct)
- ✅ `CustomUserDetailsService.java` - No changes needed (was already correct)
- ✅ `User.java` - No changes needed (implements UserDetails correctly)

### Key Classes:
- **User**: Entity that implements `UserDetails`
- **JwtService**: Handles token generation and validation
- **AuthService**: Handles registration and login logic
- **JwtFilter**: Intercepts requests and validates JWT tokens
- **CustomUserDetailsService**: Loads user details for authentication

---

## 🎯 Summary

The token mismatch error was caused by passing a String (username) instead of a UserDetails object to the JWT token generator. This has been fixed by passing the complete User object (which implements UserDetails) in both the registration and login methods. The fix ensures that JWT tokens are generated correctly with all necessary user information and can be properly validated during subsequent requests.
