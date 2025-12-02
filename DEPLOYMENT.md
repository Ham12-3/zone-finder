## AWS Deployment Guide

This document outlines how to deploy the Zone Finder backend (Spring Boot) and frontend (Next.js) on AWS.

---

### 1. Backend (Spring Boot API)

#### 1.1 Provision AWS resources
1. **RDS PostgreSQL**
   - Launch an RDS instance (for example `db.t3.micro`).
   - Create database `zonefinder` and a dedicated user/password.
   - Record hostname, port, username, password.
2. **ElastiCache Redis** (recommended for cache)
   - Create a small Redis cluster (`cache.t3.micro`).
   - Enable encryption in-transit if desired.
3. **Secrets Manager / Systems Manager Parameter Store**
   - Store database, Redis, and any API credentials here.

#### 1.2 Build and push Docker image
```bash
cd backend
mvn clean package
aws ecr create-repository --repository-name zone-finder-backend
aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <account>.dkr.ecr.<region>.amazonaws.com
docker build -t zone-finder-backend .
docker tag zone-finder-backend:latest <account>.dkr.ecr.<region>.amazonaws.com/zone-finder-backend:latest
docker push <account>.dkr.ecr.<region>.amazonaws.com/zone-finder-backend:latest
```

#### 1.3 Deploy container
Choose one:

- **ECS Fargate**
  1. Create a task definition referencing the ECR image.
  2. Define environment variables (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `REDIS_HOST`, `REDIS_PORT`, `ALLOWED_ORIGINS`, etc.).
  3. Launch a Fargate service behind an Application Load Balancer (ALB).

- **Elastic Beanstalk**
  1. Create a new environment (Java or Docker platform).
  2. Upload the jar or Docker image.
  3. Configure environment variables via EB console.

#### 1.4 Networking & CORS
1. Restrict security groups so only the ECS/EB service can reach RDS/Redis.
2. Attach an ACM certificate to the ALB and expose only HTTPS (port 443).
3. Set `cors.allowed-origins` (or env var) to include your frontend domain, e.g. `https://app.example.com`.

---

### 2. Frontend (Next.js 16)

#### Option A – AWS Amplify (recommended)
1. Push the `frontend` folder to GitHub.
2. In Amplify Console: “New app” → “Host web app”.
3. Connect the repo, select the `frontend` directory.
4. In Amplify environment variables, set:
   ```
   NEXT_PUBLIC_API_BASE_URL=https://api.example.com
   ```
5. Amplify builds (pnpm install) and deploys automatically with CI/CD.

#### Option B – S3 + CloudFront (only for static export)
1. Run `npm run build && npm run export` to generate `out/`.
2. Upload `out/` to an S3 bucket.
3. Put CloudFront in front for HTTPS and caching.
4. Note: This only works if the app is fully static (no SSR/API routes).

---

### 3. Domains & HTTPS
1. In Route 53 (or your registrar):
   - Point `api.example.com` to the backend ALB.
   - Point `app.example.com` to the Amplify app (or CloudFront distribution).
2. Ensure both have valid ACM certificates (Amplify handles its own SSL; ALB needs one associated).

---

### 4. Final Checklist
- [ ] Backend image pushed to ECR.
- [ ] RDS + Redis provisioned and accessible.
- [ ] ECS/EB service running with env vars configured.
- [ ] Amplify app deployed with `NEXT_PUBLIC_API_BASE_URL` pointing to backend.
- [ ] Custom domains + HTTPS configured.
- [ ] End-to-end testing confirms postcode lookups work in production.

---

Use this guide whenever you deploy updates to AWS. Adjust instance sizes and scaling policies as traffic grows.

