# 🎯 First Review Presentation & Live Demo Guide
**Project:** Centralized Government Opportunity Monitoring, Aggregation & Notification Platform  
**Department:** Computer Science and Engineering, Siddaganga Institute of Technology, Tumkur  
**Guide:** Dr. Anupama T A, Assistant Professor  
**Team Members:**  
- Narendra Kumar Yadav (1SI24CS114)  
- Nongthombam Sanathoi Singh (1SI24CS120)  
- Rahul Dadimana Kshetri (1SI24CS136)  
- Tomthilnganba Soraisham (1SI24CS194)  

---

## 📊 Project Completion Status: ~70%
| Objective from Review Slides | Target Status | Live Implementation Details |
| :--- | :--- | :--- |
| **Objective 1:** To automatically collect and centralize government opportunities from official sources | **100% COMPLETE** | Multi-source Data Aggregation Engine, parsing RSS feeds & web scraping with Jsoup, deduplication engine, automated `@Scheduled` background crawler, manual sync API |
| **Objective 2:** To organize, search, and filter opportunities based on user requirements and eligibility | **100% COMPLETE** | Dynamic multi-parameter JPA search & filtering by category, qualification, location, deadline urgency, plus automated candidate eligibility rule checker |
| **Objective 3:** To match opportunities with user profiles and provide relevant recommendations | **50% (IN PROGRESS / PREVIEW)** | Profile-to-Opportunity Matcher evaluating candidate age, academic percentage, and discipline compatibility across all opportunities |
| **Objective 4:** To provide timely notifications and deadline reminders while allowing users to bookmark and track applications | **50% (IN PROGRESS / PREVIEW)** | Application Tracker & Bookmarking subsystem, deadline countdown alert indicators, application lifecycle pipeline (Bookmarked → Applied → Interview → Selected) |

---

## 🚀 Live Demo Walkthrough (5-Minute Presentation Script)

### Step 1: Open the Application (0:00 - 0:30)
1. Open your browser and navigate to: **`http://localhost:5173`**
2. Show the top banner to the evaluators:
   > *"Respected guide Dr. Anupama Ma'am and review panel, we have built the working prototype implementing the core architecture from our slides. We have achieved 100% completion of Objective 1 and Objective 2, with working previews of Objective 3 and 4, taking our overall project completion to approximately 70%."*

---

### Step 2: Demonstrate Objective 1 — Data Aggregation Engine (0:30 - 2:00)
1. Click the **"Aggregation Engine"** tab in the top navigation bar.
2. Point to the **Monitored Government Sources** table:
   - Explain that the system actively monitors 7 major government agencies: **UPSC, SSC, AICTE, NSP, DRDO, DST, and ISRO**.
   - Show the extraction protocols: RSS Feeds, REST APIs, and Web Scrapers.
3. Click the blue button **"Run Full Multi-Source Aggregation"** (or click the sync icon next to any agency like UPSC or AICTE).
4. Point to the **Live Terminal Log Console** below the table:
   - Evaluators can see the live crawler executing, parsing items, checking for duplicates, and updating the database in milliseconds!
   - Highlight the **Deduplication Engine**:
     > *"As stated in Slide 10, our Data Aggregation Engine normalizes the scraped titles and deadlines, and uses cryptographic unique identifiers to prevent duplicate records."*

---

### Step 3: Demonstrate Objective 2 — Search, Filter & Eligibility Engine (2:00 - 3:30)
1. Click on the **"Browse & Search"** tab.
2. Show the **Dashboard Analytics**:
   - Total Aggregated Opportunities, Active Openings, Closing Soon (&le; 7 days), and Monitored Portals.
3. Demonstrate **Instant Search**:
   - Type `"DRDO"` or `"Civil"` or `"Engineering"` in the search bar. The grid updates immediately.
4. Demonstrate **Category Filtering**:
   - Click through the pills: *Recruitments & Jobs*, *Scholarships*, *Research Fellowships*, *Internships*, *Competitive Exams*.
5. Demonstrate **Multi-Criteria Facet Filtering**:
   - Select Qualification: **"B.Tech / B.E (Engineering)"**
   - Select Location: **"Karnataka / Bengaluru / Tumakuru"**
   - Select Deadline: **"Closing in &le; 7 Days (Urgent)"**
   - Show how the urgent openings (e.g. Smart City Internship or DRDO JRF) are instantly isolated.
6. Click on **"Check Eligibility & Details"** on any card (e.g. DRDO Scientist 'B'):
   - Scroll down to the **Live Profile Compatibility Checker**.
   - Enter: Age `24`, Percentage `75%`, Qualification `B.Tech Computer Science`.
   - Click **"Evaluate Eligibility"** → Instant **Green Checkmark** with detailed match reasons!
   - Change Age to `35` → Instant **Red Alert** explaining that age exceeds the maximum limit (28 yrs) for DRDO.

---

### Step 4: Demonstrate Objectives 3 & 4 Preview (3:30 - 4:30)
1. In the same modal, click **"Add to Application Tracker"**.
2. Click on the **"Application Tracker"** tab in the navigation:
   - Show that the opportunity is now tracked with live deadline reminders.
   - Change the pipeline status from **"Saved / Reviewing"** to **"Application Submitted"**.
3. Click on the **"Eligibility Matcher"** tab:
   - Show how the candidate can enter their profile once and evaluate all central and state opportunities simultaneously.

---

### Step 5: Architecture & Backend Proof (4:30 - 5:00)
1. Show the reviewers the backend running on Spring Boot (`http://localhost:8081/api/opportunities/stats`).
2. Show `http://localhost:8081/h2-console` (JDBC URL: `jdbc:h2:mem:govoppdb`, User: `sa`, Password: blank).
3. Mention that PostgreSQL is supported via the `postgres` Spring profile and `docker-compose.yml` matching Slide 10.

---

## ❓ Probable Review Questions & Prepared Answers

**Q1: How do you handle websites that don't have an RSS feed?**  
*Answer:* We use Jsoup for HTML DOM web scraping to extract notification tables, anchors, and deadlines, and we normalize the unstructured text into our structured `Opportunity` entity schema.

**Q2: How do you prevent the same opportunity from being added repeatedly?**  
*Answer:* Each aggregated notification is assigned a unique SHA hash / composite agency identifier (`uniqueSourceId`). When the aggregation engine runs, it checks `findByUniqueSourceId()`. If already present, it checks if the official last date was extended and updates the record, otherwise skips duplicates.

**Q3: How is this different from existing job portals?**  
*Answer:* Existing portals focus strictly on private corporate jobs or general listings. Our platform centralizes all 5 categories of government opportunities (Jobs, Scholarships, Fellowships, Internships, Exams) with automated official portal crawlers, eligibility validation, and deadline alerts in one place.

**Q4: What is your plan for the second review?**  
*Answer:* In the next review, we will complete Objective 3 (collaborative & semantic profile recommendation using candidate skill matching) and Objective 4 (automated email/SMS notifications and push notification cron jobs).
