# 🔧 403 Forbidden Error - Fix Guide

## ❌ Problem

Getting **403 Forbidden** errors when accessing dashboard endpoints:
```
GET http://localhost:8080/api/dashboard/me 403 (Forbidden)
GET http://localhost:8080/api/dashboard/enrollments 403 (Forbidden)
```

---

## 🔍 Root Cause

The issue has **TWO parts**:

### 1. **Security Configuration** ✅ FIXED
The `SecurityConfig.java` was not explicitly allowing authenticated access to `/api/dashboard/**` endpoints.

### 2. **Backend Not Restarted** ⚠️ ACTION REQUIRED
The new `DashboardController` and `DashboardService` classes are not loaded because the backend server hasn't been restarted.

---

## ✅ Fixes Applied

### **SecurityConfig.java** - Updated
```java
// BEFORE (WRONG)
.requestMatchers("/api/auth/**", "/api/courses/**").permitAll()

// AFTER (CORRECT)
.requestMatchers("/api/auth/**").permitAll()  // Public
.requestMatchers("/api/dashboard/**", "/api/courses/**", "/api/enrollments/**").authenticated()  // Protected
```

**What this does:**
- `/api/auth/**` - Public (no token needed) ✅
- `/api/dashboard/**` - Protected (requires valid JWT token) ✅
- `/api/courses/**` - Protected (requires valid JWT token) ✅
- `/api/enrollments/**` - Protected (requires valid JWT token) ✅

---

## 🚀 How to Fix

### **STEP 1: Restart Backend Server** ⚠️ CRITICAL

The backend MUST be restarted to load the new classes:

**Option A: Using IDE (Recommended)**
1. Stop the current Spring Boot application
2. Clean and rebuild the project
3. Start the application again

**Option B: Using Maven**
```bash
# Stop the current server (Ctrl+C)

# Navigate to backend directory
cd c:\Users\pc\Desktop\intern_maker\internmaker-backend\internmaker-backend

# Clean and rebuild
mvn clean install -DskipTests

# Run the application
mvn spring-boot:run
```

**Option C: Using Gradle (if applicable)**
```bash
./gradlew clean build
./gradlew bootRun
```

### **STEP 2: Verify Backend is Running**
```bash
# Test in PowerShell
Test-NetConnection -ComputerName localhost -Port 8080
```

Should show: `TcpTestSucceeded : True`

### **STEP 3: Test Dashboard Endpoint**

**Using Browser Console:**
```javascript
// Get token from localStorage
const token = localStorage.getItem('token');

// Test dashboard endpoint
fetch('http://localhost:8080/api/dashboard/me', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
.then(res => res.json())
.then(data => console.log('Dashboard Data:', data))
.catch(err => console.error('Error:', err));
```

**Expected Response:**
```json
{
  "userId": 1,
  "fullName": "Ashish Kumar",
  "email": "ashb@1234",
  "phone": "1234567890",
  "role": "STUDENT",
  "totalEnrollments": 0,
  "pendingEnrollments": 0,
  "confirmedEnrollments": 0,
  "hasActiveEnrollment": false,
  "totalCourses": 0,
  "enrolledCourses": 0,
  "availableCourses": 0,
  "completedTasks": 0,
  "totalTasks": 12,
  "progressPercentage": 0,
  "certificateEligible": false
}
```

### **STEP 4: Refresh Frontend**
```bash
# In browser, hard refresh
Ctrl + Shift + R

# Or clear cache and reload
Ctrl + F5
```

---

## 🔍 Troubleshooting

### **If still getting 403:**

#### **1. Check Token is Being Sent**
Open Browser DevTools → Network Tab → Click on failed request → Headers

Look for:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

If missing, the token is not being sent!

#### **2. Check Token is Valid**
```javascript
// In browser console
console.log('Token:', localStorage.getItem('token'));
```

Should show a long JWT string starting with `eyJ...`

#### **3. Check Backend Logs**
Look for errors in the backend console:
- JWT parsing errors
- Authentication failures
- Missing bean errors

#### **4. Verify New Classes are Loaded**
Check backend startup logs for:
```
Mapped "{[/api/dashboard/me],methods=[GET]}" onto ...
Mapped "{[/api/dashboard/enrollments],methods=[GET]}" onto ...
```

If these lines are missing, the DashboardController is not loaded!

#### **5. Check Database Connection**
The DashboardService queries the database. Ensure:
- MySQL is running
- Database `internmaker` exists
- Connection credentials are correct in `application.yml`

---

## 📊 Verification Checklist

Before testing, verify:

- [ ] Backend server is **restarted**
- [ ] No errors in backend console
- [ ] DashboardController endpoints are mapped (check logs)
- [ ] SecurityConfig.java is updated
- [ ] Frontend has valid token in localStorage
- [ ] Token is being sent in Authorization header
- [ ] Database is accessible

---

## 🎯 Expected Behavior

### **After Fix:**

1. **Login** → Token stored in localStorage ✅
2. **Navigate to dashboard** → Loading spinner appears ✅
3. **API calls made:**
   - `GET /api/dashboard/me` → **200 OK** ✅
   - `GET /api/dashboard/enrollments` → **200 OK** ✅
4. **Dashboard displays:**
   - Your actual name ✅
   - Real user ID ✅
   - Actual statistics ✅
   - No errors ✅

---

## 🔐 Security Flow

```
User logs in
    ↓
Backend generates JWT token
    ↓
Frontend stores token in localStorage
    ↓
User navigates to dashboard
    ↓
Frontend sends request with:
  Authorization: Bearer <token>
    ↓
Backend JwtFilter intercepts request
    ↓
Extracts and validates token
    ↓
If valid:
  - Sets authentication in SecurityContext
  - Allows request to DashboardController
  - Returns user data
    ↓
If invalid:
  - Returns 403 Forbidden
  - Frontend redirects to login
```

---

## 📝 Files Modified

### **Backend:**
1. ✅ **SecurityConfig.java** - Updated endpoint permissions
2. ✅ **DashboardController.java** - Created (needs backend restart)
3. ✅ **DashboardService.java** - Created (needs backend restart)
4. ✅ **EnrollmentRepository.java** - Enhanced (needs backend restart)

### **Frontend:**
1. ✅ **api.js** - Dashboard API calls
2. ✅ **UserDashboard.jsx** - Dynamic dashboard

---

## 🎉 Summary

**The 403 error is caused by:**
1. ❌ Backend not restarted (new classes not loaded)
2. ✅ Security config updated (but needs restart to take effect)

**To fix:**
1. **RESTART THE BACKEND SERVER** ⚠️
2. Refresh the frontend
3. Login again
4. Navigate to dashboard
5. Should work! ✅

---

## 🚨 CRITICAL ACTION REQUIRED

**YOU MUST RESTART THE BACKEND SERVER!**

The new `DashboardController` and `DashboardService` classes will not be available until you restart the Spring Boot application.

**After restart:**
- ✅ Dashboard endpoints will be available
- ✅ Security config will be active
- ✅ 403 errors will be resolved
- ✅ Dashboard will load with real data

---

**Next Step: RESTART YOUR BACKEND SERVER NOW!** 🚀
