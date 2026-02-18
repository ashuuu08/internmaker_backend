# ⚠️ BACKEND RESTART REQUIRED - Step-by-Step Guide

## 🚨 CRITICAL: You MUST restart the backend server!

The 403 errors are happening because the new `DashboardController` and `DashboardService` classes are not loaded.

---

## 🔧 **OPTION 1: Restart Using Your IDE (EASIEST)**

### **If using IntelliJ IDEA:**
1. Find the Spring Boot run configuration (usually at top right)
2. Click the **Stop** button (red square) ⏹️
3. Wait for it to fully stop
4. Click the **Run** button (green play) ▶️
5. Wait for "Started InternmakerBackendApplication" in console

### **If using Eclipse/STS:**
1. Go to **Console** tab
2. Click the **Terminate** button (red square)
3. Right-click on project → **Run As** → **Spring Boot App**
4. Wait for startup to complete

### **If using VS Code:**
1. Open **Terminal** in VS Code
2. Press `Ctrl+C` to stop the server
3. Run: `mvn spring-boot:run`
4. Wait for startup

---

## 🔧 **OPTION 2: Restart Using Command Line**

### **Step 1: Stop the Current Server**

**Find and kill the Java process:**
```powershell
# Find Java processes
Get-Process -Name java

# Kill the Spring Boot process (replace PID with actual process ID)
Stop-Process -Id 6664 -Force

# Or kill all Java processes (use with caution!)
Get-Process -Name java | Stop-Process -Force
```

### **Step 2: Navigate to Backend Directory**
```powershell
cd c:\Users\pc\Desktop\intern_maker\internmaker-backend\internmaker-backend
```

### **Step 3: Start the Server**

**Option A: Using Maven Wrapper (Recommended)**
```powershell
.\mvnw.cmd spring-boot:run
```

**Option B: Using Maven**
```powershell
mvn spring-boot:run
```

**Option C: Using JAR file**
```powershell
# Build first
mvn clean package -DskipTests

# Run the JAR
java -jar target\internmaker-backend-0.0.1-SNAPSHOT.jar
```

---

## ✅ **Verify Backend Started Successfully**

### **Look for these lines in the console:**

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.x.x)

...

Mapped "{[/api/dashboard/me],methods=[GET]}" onto ...
Mapped "{[/api/dashboard/enrollments],methods=[GET]}" onto ...
Mapped "{[/api/auth/login],methods=[POST]}" onto ...
Mapped "{[/api/auth/register],methods=[POST]}" onto ...

...

Started InternmakerBackendApplication in X.XXX seconds
```

**CRITICAL:** Look for the `/api/dashboard/me` and `/api/dashboard/enrollments` mappings!

If you DON'T see these, the DashboardController is NOT loaded!

---

## 🧪 **Test the Backend**

### **Test 1: Check Server is Running**
```powershell
Test-NetConnection -ComputerName localhost -Port 8080
```

Should show: `TcpTestSucceeded : True`

### **Test 2: Test Auth Endpoint (Should Work)**
```powershell
$body = @{
    email = "ashb@1234"
    password = "12345678"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body `
    -UseBasicParsing
```

Should return: `StatusCode : 200`

### **Test 3: Test Dashboard Endpoint (Should Work After Restart)**

First, get a token from login, then:

```powershell
# Replace YOUR_TOKEN_HERE with actual token from login
$token = "YOUR_TOKEN_HERE"

Invoke-WebRequest -Uri "http://localhost:8080/api/dashboard/me" `
    -Method GET `
    -Headers @{Authorization="Bearer $token"} `
    -UseBasicParsing
```

Should return: `StatusCode : 200` with user data

---

## 🔍 **Troubleshooting**

### **Problem: Can't find Java process**
```powershell
# Check if anything is using port 8080
Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
```

### **Problem: Port 8080 already in use**
```powershell
# Find what's using port 8080
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess

# Kill it
Stop-Process -Id <PID> -Force
```

### **Problem: Maven not found**
```powershell
# Check if Maven is installed
mvn --version

# If not, use the Maven wrapper
.\mvnw.cmd --version
```

### **Problem: Backend won't start**

Check for these common issues:
1. **MySQL not running** - Start MySQL service
2. **Database doesn't exist** - Create `internmaker` database
3. **Wrong password** - Check `application.yml`
4. **Port 8080 in use** - Kill the process using it

---

## 📊 **After Restart Checklist**

- [ ] Backend console shows "Started InternmakerBackendApplication"
- [ ] Console shows `/api/dashboard/me` endpoint mapping
- [ ] Console shows `/api/dashboard/enrollments` endpoint mapping
- [ ] No errors in console
- [ ] Port 8080 is accessible
- [ ] Auth endpoint works (test login)
- [ ] Dashboard endpoint works (test with token)

---

## 🎯 **Then Test Frontend**

1. **Refresh browser** (Ctrl + Shift + R)
2. **Clear localStorage** (F12 → Application → Local Storage → Clear)
3. **Login again** at `http://localhost:5173/login`
4. **Navigate to dashboard**
5. **Should work!** ✅

---

## 📝 **Quick Commands Summary**

```powershell
# Stop backend
Get-Process -Name java | Stop-Process -Force

# Navigate to backend
cd c:\Users\pc\Desktop\intern_maker\internmaker-backend\internmaker-backend

# Start backend
.\mvnw.cmd spring-boot:run

# In another terminal, test it
Test-NetConnection -ComputerName localhost -Port 8080
```

---

## 🚨 **IMPORTANT NOTES**

1. **Wait for full startup** - Don't test until you see "Started InternmakerBackendApplication"
2. **Check for endpoint mappings** - Must see `/api/dashboard/**` endpoints
3. **Clear browser cache** - Old code might be cached
4. **Login again** - Get a fresh token after restart

---

## ✅ **Expected Result After Restart**

**Backend Console:**
```
Mapped "{[/api/dashboard/me],methods=[GET]}" onto ...
Mapped "{[/api/dashboard/enrollments],methods=[GET]}" onto ...
Started InternmakerBackendApplication in 15.234 seconds
```

**Frontend:**
- ✅ Login works
- ✅ Dashboard loads
- ✅ Shows your actual name
- ✅ Displays real statistics
- ✅ No 403 errors!

---

## 🎉 **SUCCESS INDICATORS**

You'll know it worked when:
1. Backend console shows dashboard endpoint mappings
2. No 403 errors in browser console
3. Dashboard displays your name: "Ashish Kumar"
4. Stats show real data from database
5. No error messages on screen

---

**RESTART YOUR BACKEND NOW!** 🚀

After restart, the 403 errors will be gone and your dynamic dashboard will work perfectly!
