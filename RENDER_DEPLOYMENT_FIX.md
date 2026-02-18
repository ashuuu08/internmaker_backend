# 🚀 Fixing Render Deployment Failure

The deployment failed because the backend was trying to connect to a MySQL database at `localhost:3306`, which doesn't exist on Render's servers.

## ✅ Changes Made
I have updated `src/main/resources/application.yml` to use **Environment Variables**. 
This allows the app to:
1. Use a real database on Render
2. Still work perfectly on your Local Machine

---

## 🛠️ Step-by-Step Fix on Render

Follow these steps in your [Render Dashboard](https://dashboard.render.com):

### 1. Add Environment Variables
Go to your **Web Service** → **Environment** tab and add these keys:

| Key | Example Value |
| :--- | :--- |
| `DB_URL` | `jdbc:postgresql://db.YOUR_PROJECT_REF.supabase.co:5432/postgres` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `your_supabase_password` |
| `JWT_SECRET` | `Q2h6TzB0dU5Yb0JmNXJ2aWl2VUdQWnN5c2hQWkFJc3Z5dQ==` |
| `RAZORPAY_KEY_ID` | `rzp_test_SC07BKUJDCqEO4` |
| `RAZORPAY_SECRET` | `QXD6GMmX17qSsDQ1W0OZ3Rbo` |

> **Note:** Render automatically sets the `PORT` variable, so you don't need to add it manually!

### 2. Supabase Settings
Supabase uses **PostgreSQL**. Most projects use the `postgres` database by default.
- **Port:** Use `5432` for direct connection or `6543` for connection pooling.
- **SSL:** Supabase requires SSL. Add `?sslmode=require` to your `DB_URL`.
  - Example: `jdbc:postgresql://db.xxx.supabase.co:5432/postgres?sslmode=require`

### 3. Database Schema
Spring Boot will automatically create the tables if `spring.jpa.hibernate.ddl-auto` is set to `update` (which it is in `application.yml`). You just need to make sure the database (`postgres`) exists in your Supabase project.

### 4. Deploy Again
1. **Push these changes** to GitHub:
   ```powershell
   git add .
   git commit -m "chore: switch to Supabase PostgreSQL for Render deployment"
   git push origin main
   ```
2. Render should automatically start a new deploy.
3. Check the logs. You should see "Started InternmakerBackendApplication" ✅

---

## 📊 Why This Was Happening
- **Local:** Your computer has MySQL at `localhost`, so it works.
- **Render:** Render's servers are separate containers. `localhost` inside a Render container refers to the container itself, not your database! 
- **The Fix:** Using `${DB_URL}` tells Spring Boot to look at the environment settings instead of guessing.

---

**Try pushing this change now!** The "Connection Refused" error will disappear once the correct database URL is provided in Render. 🚀
