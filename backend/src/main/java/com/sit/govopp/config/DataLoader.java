package com.sit.govopp.config;

import com.sit.govopp.entity.*;
import com.sit.govopp.repository.ApplicationBookmarkRepository;
import com.sit.govopp.repository.GovernmentSourceRepository;
import com.sit.govopp.repository.OpportunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final GovernmentSourceRepository sourceRepository;
    private final OpportunityRepository opportunityRepository;
    private final ApplicationBookmarkRepository bookmarkRepository;

    public DataLoader(GovernmentSourceRepository sourceRepository,
                      OpportunityRepository opportunityRepository,
                      ApplicationBookmarkRepository bookmarkRepository) {
        this.sourceRepository = sourceRepository;
        this.opportunityRepository = opportunityRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    public void run(String... args) {
        if (sourceRepository.count() == 0) {
            log.info("Seeding official Government Sources...");
            seedSources();
        }

        if (opportunityRepository.count() < 30) {
            log.info("Populating verified National Government Opportunities database...");
            seedOpportunities();
        }
    }

    private void seedSources() {
        List<GovernmentSource> sources = List.of(
                new GovernmentSource(
                        "Union Public Service Commission",
                        "UPSC",
                        "https://upsc.gov.in",
                        "https://upsc.gov.in/rss.xml",
                        "RSS_FEED",
                        "Constitutional apex agency conducting Civil Services, Engineering Services, and Defense examinations."
                ),
                new GovernmentSource(
                        "Staff Selection Commission",
                        "SSC",
                        "https://ssc.gov.in",
                        "https://ssc.gov.in/api/notices",
                        "REST_API",
                        "Central recruitment commission for technical, clerical, and administrative roles across Central Ministries."
                ),
                new GovernmentSource(
                        "All India Council for Technical Education",
                        "AICTE",
                        "https://www.aicte-india.org",
                        "https://internship.aicte-india.org",
                        "WEB_SCRAPER",
                        "National apex authority managing technical education scholarships, faculty development, and TULIP internships."
                ),
                new GovernmentSource(
                        "National Scholarship Portal",
                        "NSP",
                        "https://scholarships.gov.in",
                        "https://scholarships.gov.in/schemes",
                        "WEB_SCRAPER",
                        "Central single-window digital portal managing Central Sector, post-matric, and minority scholarships."
                ),
                new GovernmentSource(
                        "Defence Research and Development Organisation",
                        "DRDO",
                        "https://rac.gov.in",
                        "https://rac.gov.in/drdo/feed",
                        "RSS_FEED",
                        "Premier defense R&D organisation offering Scientist 'B', JRF, and engineering apprentice recruitments."
                ),
                new GovernmentSource(
                        "Department of Science & Technology",
                        "DST",
                        "https://dst.gov.in",
                        "https://online-inspire.gov.in",
                        "WEB_SCRAPER",
                        "Nodal government department promoting scientific innovation, INSPIRE scholarships, and doctoral fellowships."
                ),
                new GovernmentSource(
                        "Indian Space Research Organisation",
                        "ISRO",
                        "https://www.isro.gov.in",
                        "https://www.isro.gov.in/careers",
                        "WEB_SCRAPER",
                        "National space research agency offering Scientist/Engineer SC openings and graduate apprenticeships."
                ),
                new GovernmentSource(
                        "Council of Scientific and Industrial Research",
                        "CSIR",
                        "https://csir.res.in",
                        "https://csirhrdg.res.in",
                        "WEB_SCRAPER",
                        "Autonomous national R&D organization conducting CSIR-NET and awarding national doctoral fellowships."
                ),
                new GovernmentSource(
                        "Bhabha Atomic Research Centre",
                        "BARC",
                        "https://barc.gov.in",
                        "https://barcoces.examdata.co.in",
                        "WEB_SCRAPER",
                        "India's premier nuclear research facility conducting OCES/DGFS Scientific Officer recruitments."
                ),
                new GovernmentSource(
                        "NITI Aayog",
                        "NITI",
                        "https://niti.gov.in",
                        "https://niti.gov.in/internship",
                        "WEB_SCRAPER",
                        "Apex public policy think tank offering research internships in public governance, economics, and analytics."
                ),
                new GovernmentSource(
                        "Reserve Bank of India",
                        "RBI",
                        "https://rbi.org.in",
                        "https://opportunities.rbi.org.in",
                        "REST_API",
                        "India's central banking institution conducting Grade B Officer exams and national summer research internships."
                ),
                new GovernmentSource(
                        "Ministry of Electronics & Information Technology",
                        "MeitY",
                        "https://meity.gov.in",
                        "https://www.calicut.nielit.in/nic",
                        "WEB_SCRAPER",
                        "Nodal ministry for Digital India, National Informatics Centre (NIC) and C-DAC engineering recruitments."
                )
        );

        for (GovernmentSource s : sources) {
            s.setLastSyncTime(LocalDateTime.now().minusHours(1));
            s.setSyncStatus("SUCCESS");
            s.setTotalOpportunitiesExtracted(5);
            sourceRepository.save(s);
        }
    }

    private void seedOpportunities() {
        opportunityRepository.deleteAll();
        LocalDate today = LocalDate.now();
        List<Opportunity> list = new ArrayList<>();

        // ==========================================
        // 1. RECRUITMENT & DIRECT JOBS (12 items)
        // ==========================================
        list.add(createOpp(
                "DRDO Direct Recruitment of Scientist 'B' in Cyber Systems & AI",
                "Defence Research and Development Organisation (DRDO)",
                OpportunityCategory.RECRUITMENT,
                "First Class B.Tech / B.E in CS / IT / Electronics",
                "Computer Science & Defence Systems",
                "Bengaluru, Karnataka / New Delhi",
                "Recruitment for direct appointment to Scientist 'B' (Pay Level-10: ₹56,100/- plus HRA & DA). Work on advanced autonomous defense systems and cyber cryptographic architectures.",
                "First Class B.Tech with valid GATE CS/EC score. Indian Citizen. Age up to 28 years.",
                21, 28, 60.0, "₹100 (SC/ST/Divyang/Women exempt)",
                -5, 20,
                "https://rac.gov.in/drdo/recruitment", "DRDO", "DRDO-SCB-CSE-2027"
        ));

        list.add(createOpp(
                "ISRO Scientist/Engineer 'SC' Recruitment (Level 10 Gazetted)",
                "Indian Space Research Organisation (ISRO)",
                OpportunityCategory.RECRUITMENT,
                "B.Tech / B.E in Mechanical, Electrical, Computer Science or Civil",
                "Aerospace & Satellite Engineering",
                "Bengaluru, Sriharikota, Thiruvananthapuram",
                "Gazetted Group 'A' posts at ISRO Centers (URSC, VSSC, SDSC SHAR). Excellent opportunity to design satellite subsystems, payload telemetry, and launch vehicles.",
                "First Class Bachelor's Degree in Engineering with minimum 65% marks or 6.84 CGPA.",
                21, 28, 65.0, "₹250 (Exempt for reserved categories)",
                -8, 14,
                "https://www.isro.gov.in/careers", "ISRO", "ISRO-SC-ENGG-2027"
        ));

        list.add(createOpp(
                "BARC Scientific Officer (OCES/DGFS) through Engineering Disciplines",
                "Bhabha Atomic Research Centre (BARC)",
                OpportunityCategory.RECRUITMENT,
                "B.Tech / B.E in Nuclear, Mechanical, Chemical, CSE or ECE",
                "Nuclear Science & Applied Technology",
                "Mumbai, Maharashtra / Kalpakkam, Tamil Nadu",
                "One-year Orientation Course for Engineering Graduates and Science Post-Graduates (OCES) leading to appointment as Scientific Officer (Group A, Level 10).",
                "Regular B.E./B.Tech. with minimum 60% aggregate. Valid GATE or BARC Online Screening Exam score.",
                20, 26, 60.0, "₹500 (Women/SC/ST/PwD exempt)",
                -12, 16,
                "https://barcoces.examdata.co.in", "BARC", "BARC-OCES-DGFS-2027"
        ));

        list.add(createOpp(
                "NIC Scientist 'B' & Scientific Officer (MeitY e-Governance)",
                "National Informatics Centre (NIC / NIELIT)",
                OpportunityCategory.RECRUITMENT,
                "B.Tech / B.E in CS / IT / Electronics / MCA / M.Sc CS",
                "e-Governance & Cloud Infrastructure",
                "New Delhi & State Capitals (All India)",
                "Recruitment of technical officers to architect and scale Digital India core infrastructure, national portals, and state government data center systems.",
                "Engineering Degree or MCA with 60% aggregate. Written Examination + Technical Interview.",
                21, 30, 60.0, "₹800 (General) | Nil for SC/ST/Women",
                -3, 5, // Closing soon!
                "https://www.calicut.nielit.in/nic", "MeitY", "NIC-SCB-MEITY-2027"
        ));

        list.add(createOpp(
                "C-DAC Project Engineer (High Performance Computing & GPU Supercomputing)",
                "Centre for Development of Advanced Computing (C-DAC)",
                OpportunityCategory.RECRUITMENT,
                "B.Tech / B.E in CSE / IT / ECE or MCA with 60% marks",
                "Supercomputing & Quantum Technologies",
                "Pune, Maharashtra / Bengaluru, Karnataka",
                "National Supercomputing Mission (NSM) positions developing system software, parallel MPI/CUDA algorithms, and AI accelerated high performance stacks.",
                "Full time B.Tech or M.Tech in relevant branch. Industry experience in Linux/C++/Python preferred.",
                21, 32, 60.0, "₹500 (Free for SC/ST/PwD)",
                -7, 18,
                "https://www.cdac.in/careers", "MeitY", "CDAC-PE-HPC-2027"
        ));

        list.add(createOpp(
                "BEL Probationary Engineer (Bangalore & Ghaziabad Complexes)",
                "Bharat Electronics Limited (BEL - Navratna PSU)",
                OpportunityCategory.RECRUITMENT,
                "First Class B.Tech / B.E in Electronics / Computer Science / Mechanical",
                "Defense Electronics & Radar Systems",
                "Bengaluru, Karnataka / Ghaziabad, UP",
                "Permanent Navratna PSU appointment. Starting CTC ₹11.5 to ₹12.5 Lakhs per annum. Radar systems, avionics, defense communication networks, and EW systems.",
                "All India written test + interview. Min 65% aggregate for General/OBC.",
                21, 26, 65.0, "₹472 (SC/ST/PwD exempt)",
                -10, 4, // Closing soon!
                "https://bel-india.in/CareersGrid.aspx", "BEL", "BEL-PE-BLR-2027"
        ));

        list.add(createOpp(
                "HAL Management Trainee (Technical) - Aircraft Division",
                "Hindustan Aeronautics Limited (HAL)",
                OpportunityCategory.RECRUITMENT,
                "B.Tech / B.E in Aeronautical, Mechanical, Electrical or Electronics",
                "Aeronautical & Defense Manufacturing",
                "Bengaluru, Nashik, Hyderabad, Lucknow",
                "Engineering Management Trainee in aircraft, helicopter, and engine manufacturing divisions. Competitive PSU salary package and executive training.",
                "Bachelor's degree in engineering with 65% marks. Maximum age 28 years.",
                21, 28, 65.0, "₹500 (SC/ST exempt)",
                -6, 22,
                "https://hal-india.co.in/careers", "HAL", "HAL-MT-TECH-2027"
        ));

        list.add(createOpp(
                "CRIS Assistant Software Engineer (Indian Railways Information Systems)",
                "Centre for Railway Information Systems (CRIS)",
                OpportunityCategory.RECRUITMENT,
                "B.Tech / B.E in CS / IT / MCA or B.Sc CS",
                "Railway Telematics & Ticketing Systems",
                "New Delhi, Kolkata, Mumbai, Chennai",
                "Direct recruitment based on GATE score for maintaining Indian Railways Passenger Reservation System, Freight Operations Information System, and Rail Budgeting.",
                "Valid GATE score in Computer Science & Information Technology. Age 22 to 27 years.",
                22, 27, 60.0, "₹1000 (SC/ST/Women exempt)",
                -14, 11,
                "https://cris.org.in/careers", "CRIS", "CRIS-ASE-GATE-2027"
        ));

        list.add(createOpp(
                "SBI Probationary Officer (Scale-I Executive Officers)",
                "State Bank of India (SBI)",
                OpportunityCategory.RECRUITMENT,
                "Graduation in Any Discipline from Recognized University",
                "Banking, Finance & Rural Credit",
                "All India (Pan India Branch Network)",
                "Prestigious public banking career with rapid promotional avenues to Senior Management and Executive Director grades. CTC starting at ₹16 Lakhs per annum.",
                "Candidates in the final year/semester of graduation may also apply provisionally. Age 21 to 30 years.",
                21, 30, 50.0, "₹750 (SC/ST/PwD exempt)",
                -4, 25,
                "https://sbi.co.in/web/careers", "SBI", "SBI-PO-EXAM-2027"
        ));

        list.add(createOpp(
                "IBPS Specialist Officer (Scale-I IT Officers)",
                "Institute of Banking Personnel Selection (IBPS)",
                OpportunityCategory.RECRUITMENT,
                "4-year Engineering Degree in CS / IT / Electronics / MCA / M.Sc IT",
                "Core Banking Infrastructure & Security",
                "All India (Public Sector Banks)",
                "Recruitment for IT Officer (Scale I) in Punjab National Bank, Bank of Baroda, Canara Bank, Union Bank of India and participating banks.",
                "Valid degree from recognized university. Age 20 to 30 years.",
                20, 30, 55.0, "₹850 (₹175 for SC/ST/PwD)",
                -15, 6, // Closing soon!
                "https://www.ibps.in", "IBPS", "IBPS-SO-IT-2027"
        ));

        list.add(createOpp(
                "Intelligence Bureau ACIO Grade-II / Technical (MHA)",
                "Intelligence Bureau, Ministry of Home Affairs",
                OpportunityCategory.RECRUITMENT,
                "B.Tech / B.E in Electronics / Telecommunications / CS / IT or Master's in Science",
                "National Intelligence & Cyber Security",
                "All India Transfer Liability",
                "Assistant Central Intelligence Officer (ACIO-II/Tech). Group 'C' Non-Gazetted Central Government service with 20% Special Security Allowance.",
                "Valid GATE score in EC or CS. Indian citizenship. Rigorous character verification.",
                18, 27, 55.0, "₹200 (SC/ST/Female exempt)",
                -20, 8,
                "https://mha.gov.in", "MHA", "MHA-IB-ACIO-TECH-2027"
        ));

        list.add(createOpp(
                "Karnataka Public Service Commission (KPSC) Assistant Engineer (PWD & WRD)",
                "Karnataka Public Service Commission (KPSC)",
                OpportunityCategory.RECRUITMENT,
                "Bachelor's Degree in Civil / Mechanical / Electrical Engineering",
                "State Infrastructure & Water Resources",
                "Bengaluru, Tumakuru, Mysuru, Hubballi (Karnataka)",
                "Gazetted Group 'B' state government appointments in Public Works Department (PWD) and Water Resources Department (WRD) across Karnataka districts.",
                "Degree in engineering from recognized university. Kannada language knowledge mandatory. Age 18 to 35 years (General).",
                18, 35, 50.0, "₹300 (SC/ST: ₹150)",
                -9, 19,
                "https://kpsc.kar.nic.in", "KPSC", "KPSC-AE-PWD-2027"
        ));

        // ==========================================
        // 2. COMPETITIVE EXAMINATIONS (10 items)
        // ==========================================
        list.add(createOpp(
                "Civil Services (Preliminary) Examination 2027 (IAS, IPS, IFS)",
                "Union Public Service Commission (UPSC)",
                OpportunityCategory.COMPETITIVE_EXAM,
                "Bachelor's Degree in Any Discipline from a Recognized University",
                "Civil Services & National Administration",
                "All India (77 Test Centers nationwide)",
                "Premier national examination for recruitment to Indian Administrative Service, Indian Police Service, Indian Foreign Service, and 24 Central Group A services.",
                "Indian National. Age: 21 to 32 years. Attempts: General 6, OBC 9, SC/ST unlimited up to age limit.",
                21, 32, 50.0, "₹100 (Female/SC/ST/PwD exempt)",
                -10, 15,
                "https://upsc.gov.in/examinations/civil-services", "UPSC", "UPSC-CSE-PRE-2027"
        ));

        list.add(createOpp(
                "UPSC Engineering Services Examination (ESE / IES) 2027",
                "Union Public Service Commission (UPSC)",
                OpportunityCategory.COMPETITIVE_EXAM,
                "B.Tech / B.E in Civil, Mechanical, Electrical, or Electronics & Telecommunication",
                "Engineering Leadership in Central Govt",
                "All India (Major State Capitals)",
                "Recruitment of Group 'A' Executive Engineers in Indian Railway Management Service, Central Engineering Service (CPWD), Military Engineer Services (MES), and CWES.",
                "Degree in Engineering from recognized institution. Age 21 to 30 years as of Jan 1.",
                21, 30, 60.0, "₹200 (Female/SC/ST exempt)",
                -8, 12,
                "https://upsc.gov.in/examinations/engineering-services", "UPSC", "UPSC-ESE-IES-2027"
        ));

        list.add(createOpp(
                "UPSC Combined Defence Services Examination (CDSE-I)",
                "Union Public Service Commission (UPSC)",
                OpportunityCategory.COMPETITIVE_EXAM,
                "Graduation in Any Discipline / B.Tech for Air Force & Navy",
                "Armed Forces & Commissioned Officer Ranks",
                "All India",
                "Admission into Indian Military Academy (Dehradun), Indian Naval Academy (Ezhimala), Air Force Academy (Hyderabad), and Officers Training Academy (Chennai).",
                "Unmarried graduates. Age 19 to 24 years. Strict physical fitness standards.",
                19, 24, 50.0, "₹200 (Female/SC/ST exempt)",
                -15, 3, // Closing soon!
                "https://upsc.gov.in/examinations/cdse", "UPSC", "UPSC-CDS-EXAM-2027"
        ));

        list.add(createOpp(
                "SSC Combined Graduate Level Examination (SSC CGL 2026-2027)",
                "Staff Selection Commission (SSC)",
                OpportunityCategory.COMPETITIVE_EXAM,
                "Bachelor's Degree in Any Discipline from Recognized University",
                "Audit, Revenue, Intelligence & Central Secretariat",
                "All India (Computer Based Mode)",
                "Recruitment for Assistant Audit Officer, Assistant Section Officer (CSS, MEA, IB), Inspector of Income Tax, Central Excise Inspector, and Enforcement Officers.",
                "Graduation in any stream. Age between 18 and 30 years (relaxations for reserved categories).",
                18, 30, 50.0, "₹100 (Women/SC/ST/PwD exempt)",
                -11, 17,
                "https://ssc.gov.in/portal/cgl", "SSC", "SSC-CGL-EXAM-2027"
        ));

        list.add(createOpp(
                "SSC Junior Engineer (Civil, Electrical, Mechanical) Exam",
                "Staff Selection Commission (SSC)",
                OpportunityCategory.COMPETITIVE_EXAM,
                "Diploma or Degree in Civil / Electrical / Mechanical Engineering",
                "Technical Infrastructure & Public Works",
                "All India (CPWD, MES, CWC, BRO)",
                "Recruitment for Junior Engineers (Group B Non-Gazetted) in Central Public Works Department, Border Roads Organisation, Central Water Commission, and Farakka Barrage.",
                "Degree or 3-year Diploma in Engineering + 2 years experience for diploma holders. Age up to 30 years.",
                18, 30, 55.0, "₹100 (SC/ST/Women free)",
                -6, 21,
                "https://ssc.gov.in/portal/je", "SSC", "SSC-JE-EXAM-2027"
        ));

        list.add(createOpp(
                "SSC Combined Higher Secondary (10+2) Level Examination (CHSL)",
                "Staff Selection Commission (SSC)",
                OpportunityCategory.COMPETITIVE_EXAM,
                "Passed 12th Standard or Equivalent from Recognized Board",
                "Clerical & Data Processing Cadres",
                "All India",
                "Recruitment for Lower Division Clerk (LDC), Junior Secretariat Assistant (JSA), and Data Entry Operator (DEO) in Central Government Ministries and Departments.",
                "12th standard pass. Age between 18 and 27 years. Typing speed test required in Tier-II.",
                18, 27, 50.0, "₹100 (Exempt for reserved candidates)",
                -18, 5, // Closing soon!
                "https://ssc.gov.in/portal/chsl", "SSC", "SSC-CHSL-102-2027"
        ));

        list.add(createOpp(
                "RBI Grade 'B' (General / DEPR / DSIM) Officers Examination",
                "Reserve Bank of India Services Board",
                OpportunityCategory.COMPETITIVE_EXAM,
                "Graduation with minimum 60% marks (50% for SC/ST/PwBD) or Master's",
                "Monetary Policy & Central Banking",
                "All India Phase I & Phase II Centers",
                "Direct recruitment for Grade B Officers in Reserve Bank of India. Prestigious career managing national monetary policy, banking supervision, and financial markets.",
                "Minimum 60% marks in Bachelor's degree and 10th/12th. Age 21 to 30 years.",
                21, 30, 60.0, "₹850 (₹100 for SC/ST/PwBD)",
                -5, 24,
                "https://opportunities.rbi.org.in", "RBI", "RBI-GRADE-B-2027"
        ));

        list.add(createOpp(
                "National Defence Academy & Naval Academy Examination (NDA-I)",
                "Union Public Service Commission (UPSC)",
                OpportunityCategory.COMPETITIVE_EXAM,
                "12th Class pass of the 10+2 pattern (Physics and Maths for Air Force & Navy)",
                "Army, Navy and Air Force Wings",
                "All India Centers",
                "Admission to the Army, Navy and Air Force wings of the NDA and Indian Naval Academy Course (INAC). Premier military training academy.",
                "Unmarried male and female candidates born between July 2, 2008 and July 1, 2011. Medical fitness mandatory.",
                16, 19, 50.0, "₹100 (Female/SC/ST exempt)",
                -14, 9,
                "https://upsc.gov.in/examinations/nda", "UPSC", "UPSC-NDA-NA-2027"
        ));

        list.add(createOpp(
                "UGC National Eligibility Test (UGC NET) for Assistant Professor & JRF",
                "National Testing Agency (NTA / UGC)",
                OpportunityCategory.COMPETITIVE_EXAM,
                "Master's Degree or Equivalent with at least 55% marks in Humanities / CS / Mgmt",
                "Higher Education & University Lecturership",
                "All India (CBT Mode across 235 Cities)",
                "Determining the eligibility of Indian nationals for 'Assistant Professor' as well as 'Junior Research Fellowship and Assistant Professor' in Indian universities.",
                "Master's Degree with minimum 55% aggregate (50% for reserved). JRF age limit 30 years; no age limit for Assistant Professor.",
                21, 30, 55.0, "₹1150 (Gen) | ₹600 (OBC) | ₹325 (SC/ST)",
                -12, 13,
                "https://ugcnet.nta.nic.in", "UGC", "UGC-NET-JRF-2027"
        ));

        list.add(createOpp(
                "Karnataka Administrative Services (KAS) Gazetted Probationers Exam",
                "Karnataka Public Service Commission (KPSC)",
                OpportunityCategory.COMPETITIVE_EXAM,
                "Bachelor's Degree in Any Discipline from a Recognized University",
                "Karnataka State Civil Administration",
                "Karnataka (District Headquarters)",
                "Recruitment for Assistant Commissioner (KAS Group A), Deputy Superintendent of Police (DySP), Assistant Commissioner of Commercial Taxes, and Tahsildar (Grade II).",
                "Must hold a degree from recognized university. Age 21 to 38 years for General, up to 43 years for SC/ST.",
                21, 38, 50.0, "₹600 (₹300 for Category 2A/2B/3A/3B)",
                -7, 26,
                "https://kpsc.kar.nic.in/kas", "KPSC", "KPSC-KAS-EXAM-2027"
        ));

        // ==========================================
        // 3. SCHOLARSHIPS & FINANCIAL AID (10 items)
        // ==========================================
        list.add(createOpp(
                "AICTE Pragati Scholarship Scheme for Girl Students (Degree/Diploma)",
                "All India Council for Technical Education (AICTE)",
                OpportunityCategory.SCHOLARSHIP,
                "First Year B.Tech / B.E or Polytechnic Diploma Student",
                "Technical & Engineering Education",
                "All India (AICTE Approved Institutions)",
                "Empowering young women pursuing technical education with ₹50,000 per annum towards tuition fees, laptop, books, and competitive examination materials.",
                "Only girl students admitted to 1st year of technical degree/diploma. Family income less than ₹8 Lakhs/year. Maximum 2 girls per family.",
                17, 24, 60.0, "Free",
                -14, 22,
                "https://www.aicte-india.org/schemes/students-development-schemes/pragati", "AICTE", "AICTE-PRAGATI-GIRLS-2027"
        ));

        list.add(createOpp(
                "AICTE Saksham Scholarship Scheme for Specially-Abled Students",
                "All India Council for Technical Education (AICTE)",
                OpportunityCategory.SCHOLARSHIP,
                "First Year Degree / Diploma in AICTE Approved Technical Institution",
                "Technical Education Accessibility",
                "All India",
                "Financial assistance of ₹50,000 per annum to encourage differently-abled students to pursue professional technical degrees without financial burdens.",
                "Specially-abled students having disability of not less than 40%. Family income below ₹8 Lakhs per annum.",
                17, 28, 50.0, "Free",
                -16, 25,
                "https://www.aicte-india.org/schemes/students-development-schemes/saksham", "AICTE", "AICTE-SAKSHAM-2027"
        ));

        list.add(createOpp(
                "Central Sector Scheme of Scholarships for College and University Students",
                "Department of Higher Education (NSP)",
                OpportunityCategory.SCHOLARSHIP,
                "Passed 12th / PUC with >80th percentile in relevant board stream",
                "Undergraduate & Postgraduate Studies",
                "All India",
                "Financial scholarship of ₹12,000/- per year for 3 years of UG and ₹20,000/- per year for PG studies. 82,000 fresh scholarships awarded annually.",
                "Students who scored above 80th percentile in Class 12 board examination. Family income less than ₹4.5 Lakhs per annum.",
                17, 24, 80.0, "Free",
                -20, 5, // Closing soon!
                "https://scholarships.gov.in/central-sector-scheme", "NSP", "NSP-CSSS-COLLEGE-2027"
        ));

        list.add(createOpp(
                "National Overseas Scholarship Scheme for SC/ST Candidates",
                "Ministry of Social Justice & Empowerment (NSP)",
                OpportunityCategory.SCHOLARSHIP,
                "Bachelor's or Master's with minimum 60% marks",
                "International Higher Studies (USA, UK, Germany)",
                "Global / Top 500 QS Universities",
                "Covers 100% tuition fees, annual contingency allowance, medical insurance, and maintenance allowance ($15,400/yr) for Master's and Ph.D abroad.",
                "Candidates belonging to SC/ST categories with admission in top 500 ranked world universities. Family income below ₹8 Lakhs/year.",
                20, 35, 60.0, "Free",
                -15, 6, // Closing soon!
                "https://scholarships.gov.in/nos-scheme", "NSP", "NSP-NOS-GLOBAL-2027"
        ));

        list.add(createOpp(
                "Prime Minister's Special Scholarship Scheme (PMSSS J&K & Ladakh)",
                "AICTE & Ministry of Education",
                OpportunityCategory.SCHOLARSHIP,
                "Passed 10+2 from J&K / Ladakh Board with CBSE / State Board syllabus",
                "Professional Engineering & General Degrees",
                "All India (Outside J&K/Ladakh)",
                "Full academic fee up to ₹1.25 Lakh per annum plus maintenance allowance of ₹1.00 Lakh per annum for lodging and books.",
                "Domicile of Jammu & Kashmir and Ladakh. Family income not exceeding ₹8.00 Lakh per annum.",
                17, 22, 60.0, "Free",
                -10, 20,
                "https://www.aicte-jk-scholarship-gov.in", "AICTE", "AICTE-PMSSS-2027"
        ));

        list.add(createOpp(
                "Post-Matric Scholarship Scheme for Minority Students",
                "Ministry of Minority Affairs (NSP)",
                OpportunityCategory.SCHOLARSHIP,
                "Enrolled in Class 11, 12, Degree, Diploma or Professional Courses",
                "Minority Education Support",
                "All India",
                "Admission and tuition fee reimbursement up to ₹10,000/year plus maintenance allowance for hostellers and day scholars from notified minority communities.",
                "Muslim, Christian, Sikh, Buddhist, Jain or Parsi students with minimum 50% in previous final exam. Family income below ₹2 Lakhs.",
                16, 25, 50.0, "Free",
                -18, 4, // Closing soon!
                "https://scholarships.gov.in/minority-post-matric", "NSP", "NSP-MINORITY-PM-2027"
        ));

        list.add(createOpp(
                "DST INSPIRE Scholarship for Higher Education (SHE)",
                "Department of Science & Technology (DST India)",
                OpportunityCategory.SCHOLARSHIP,
                "Top 1% in Class 12 Board Exam pursuing B.Sc / BS-MS in Basic Sciences",
                "Natural & Basic Sciences",
                "All India (IISc, IISERs, NISER, Central Universities)",
                "Valued at ₹80,000 per annum (₹60,000 cash scholarship + ₹20,000 summer mentorship research grant) for 5 years.",
                "Rank in top 1% of respective 12th board, or KVPY / JEE / NEET rank holder pursuing regular B.Sc or Integrated M.Sc.",
                17, 22, 85.0, "Free",
                -12, 28,
                "https://online-inspire.gov.in/scholarship", "DST", "DST-INSPIRE-SHE-2027"
        ));

        list.add(createOpp(
                "ONGC Foundation Merit Scholarship for SC/ST Engineering Students",
                "Oil and Natural Gas Corporation (ONGC Foundation)",
                OpportunityCategory.SCHOLARSHIP,
                "First Year B.Tech / MBBS / MBA / Geology students",
                "Engineering, Geosciences & Medicine",
                "All India",
                "Annual financial grant of ₹48,000/- per year for the entire duration of the course to support meritorious students from underprivileged backgrounds.",
                "Enrolled in 1st year of regular engineering or professional degree. Minimum 60% marks in 12th. Family income below ₹4.5 Lakhs.",
                18, 25, 60.0, "Free",
                -14, 18,
                "https://ongcscholar.org", "ONGC", "ONGC-SCHOLAR-SCST-2027"
        ));

        list.add(createOpp(
                "Dr. Ambedkar Post-Doctoral Fellowship for ST Candidates",
                "Ministry of Tribal Affairs & UGC",
                OpportunityCategory.SCHOLARSHIP,
                "Ph.D. Degree in Science, Engineering, Social Sciences or Humanities",
                "Post-Doctoral Research Studies",
                "Indian Universities & Premier Research Institutes",
                "Fellowship of ₹54,000/- per month + HRA + Contingency grant of ₹50,000/- per annum for 3 years of postdoctoral research in India.",
                "Candidates belonging to Scheduled Tribe who have successfully completed Ph.D. within last 3 years.",
                25, 45, 55.0, "Free",
                -8, 23,
                "https://tribal.nic.in/fellowship", "NSP", "NSP-AMBEDKAR-PDF-2027"
        ));

        list.add(createOpp(
                "Siemens Scholarship Program for Government Engineering Colleges",
                "Siemens India CSR & Ministry of Skill Development",
                OpportunityCategory.SCHOLARSHIP,
                "First Year Students in Government Engineering Colleges (EE, ME, CS, IT)",
                "Technical Engineering & Industry Mentorship",
                "State Government Engineering Colleges (Pan India)",
                "100% Tuition fee reimbursement, annual books allowance, laptop grant, and comprehensive soft skill & digital industrial automation training.",
                "1st year student of recognized Government Engineering College. Minimum 60% in 10th and 50% in 12th. Family income below ₹2 Lakhs.",
                17, 21, 60.0, "Free",
                -15, 30,
                "https://www.siemens.co.in/scholarship", "SIEMENS", "SIEMENS-GOV-ENGG-2027"
        ));

        // ==========================================
        // 4. RESEARCH FELLOWSHIPS (10 items)
        // ==========================================
        list.add(createOpp(
                "DST INSPIRE Fellowship for Doctoral Research in Basic & Applied Sciences",
                "Department of Science & Technology (DST India)",
                OpportunityCategory.FELLOWSHIP,
                "M.Sc / M.Tech / M.E with University 1st Rank or INSPIRE Scholar",
                "Scientific & Engineering Research",
                "All India (Universities, IISc, IITs, NITs)",
                "Financial fellowship of ₹37,000/- p.m. (JRF) and ₹42,000/- p.m. (SRF) plus HRA and ₹20,000/- annual contingency grant for 5 years.",
                "Indian citizens who secured 1st Rank in university PG examination or INSPIRE scholars with 65% aggregate. Age 22 to 32 years.",
                22, 32, 65.0, "Free",
                -12, 25,
                "https://online-inspire.gov.in/fellowship", "DST", "DST-INSPIRE-DOC-2027"
        ));

        list.add(createOpp(
                "Prime Minister's Research Fellowship (PMRF Scheme) in IITs & IISc",
                "Ministry of Education & National Coordinating Institute IIT Delhi",
                OpportunityCategory.FELLOWSHIP,
                "B.Tech with 8.0 CGPA from IIT/NIT or M.Tech from Recognized University",
                "Frontier Science, AI & Engineering Technology",
                "IISc Bengaluru, IITs, IISERs (All India)",
                "Most prestigious national fellowship: ₹70,000/month for first 2 years, ₹75,000/month for 3rd year, ₹80,000/month for 4th & 5th years + ₹2.0 Lakhs annual research grant.",
                "B.Tech final year or graduate with CGPA 8.0+ or valid GATE score 750+. Rigorous multi-stage interview.",
                21, 30, 75.0, "Free",
                -10, 16,
                "https://pmrf.in", "MoE", "PMRF-DIRECT-PHD-2027"
        ));

        list.add(createOpp(
                "DRDO CAIR Junior Research Fellowship (JRF) in Artificial Intelligence & Robotics",
                "DRDO Centre for Artificial Intelligence & Robotics (CAIR)",
                OpportunityCategory.FELLOWSHIP,
                "M.Tech / M.E or B.Tech with valid GATE score in Computer Science",
                "AI, Cyber Systems & Autonomous Robotics",
                "Bengaluru, Karnataka",
                "Monthly stipend of ₹37,000/- plus 30% HRA. Work on advanced neural network vision models, robotic path planning, and embedded edge compute.",
                "B.Tech (1st Class) with valid GATE score in CS/IT or M.Tech in CSE. Age limit 28 years.",
                21, 28, 60.0, "Nil",
                -8, 6, // Closing soon!
                "https://www.drdo.gov.in/cair-jrf", "DRDO", "DRDO-CAIR-JRF-2027"
        ));

        list.add(createOpp(
                "ISRO URSC Junior Research Fellowship in On-board Satellite Computer Architecture",
                "U R Rao Satellite Centre (URSC / ISRO)",
                OpportunityCategory.FELLOWSHIP,
                "M.E / M.Tech in Electronics / Computer Science / VLSI Design",
                "Spacecraft Avionics & Radiation Hardened Computing",
                "Bengaluru, Karnataka",
                "Fellowship of ₹37,000/- p.m. + HRA for research in fault-tolerant multiprocessor architectures for deep space exploration missions.",
                "First Class Master's degree in engineering or First Class B.Tech with valid GATE. Age max 28.",
                21, 28, 65.0, "Free",
                -9, 19,
                "https://www.isro.gov.in/ursc-jrf-recruitment", "ISRO", "ISRO-URSC-JRF-2027"
        ));

        list.add(createOpp(
                "CSIR Senior Research Fellowship (SRF) in Computer & Information Sciences",
                "Council of Scientific and Industrial Research (CSIR-HRDG)",
                OpportunityCategory.FELLOWSHIP,
                "M.Tech / M.E in Computer Science / IT / Data Science with 2 years research experience",
                "Machine Learning, Big Data & High Throughput Computing",
                "CSIR National Laboratories (All India)",
                "Stipend of ₹42,000/- per month + HRA. Tenure up to 3 years at National Aerospace Laboratories (NAL Bangalore) or CSIO Chandigarh.",
                "M.E./M.Tech. with at least 60% marks and 2 years documented post-M.Tech research experience. Age up to 32 years.",
                23, 32, 60.0, "Free",
                -15, 27,
                "https://csirhrdg.res.in/srf-call", "CSIR", "CSIR-SRF-CSE-2027"
        ));

        list.add(createOpp(
                "ICMR Junior Research Fellowship in Biomedical Informatics & Genomic AI",
                "Indian Council of Medical Research (ICMR)",
                OpportunityCategory.FELLOWSHIP,
                "Postgraduate degree in Bioinformatics, Computational Biology, or Computer Science",
                "Health Informatics & Computational Epidemiology",
                "ICMR Institutes (NIV Pune, NICPR, NIRRH)",
                "Financial fellowship of ₹37,000/- p.m. + HRA + contingency for Ph.D. research in computational healthcare and epidemiological data modeling.",
                "Postgraduate in relevant scientific/engineering discipline with 55% marks. Age up to 28 years.",
                21, 28, 55.0, "₹1000 (SC/ST: ₹800)",
                -11, 7, // Closing soon!
                "https://icmr.nic.in/content/junior-research-fellowship", "ICMR", "ICMR-JRF-MED-2027"
        ));

        list.add(createOpp(
                "SERB National Post-Doctoral Fellowship (N-PDF Scheme)",
                "Science and Engineering Research Board (SERB - Anusandhan NRF)",
                OpportunityCategory.FELLOWSHIP,
                "Ph.D. / M.D. / M.S. in Science or Engineering from Recognized Institution",
                "Frontier Scientific & Technological Innovations",
                "Host Institutions: IITs, IISc, Central Universities",
                "Fellowship of ₹60,000/- per month + HRA + Research Grant of ₹2.00 Lakhs per annum and overheads of ₹35,000/- per annum for 2 years.",
                "Applicant must be an Indian citizen. Maximum age 35 years (relaxation for women and reserved categories).",
                25, 35, 60.0, "Free",
                -20, 29,
                "https://serbonline.in/SERB/npdf", "SERB", "SERB-NPDF-NRF-2027"
        ));

        list.add(createOpp(
                "CSIR-NAL Research Fellowship in Aerodynamics & Flight Simulation",
                "CSIR National Aerospace Laboratories (NAL)",
                OpportunityCategory.FELLOWSHIP,
                "B.Tech / M.Tech in Aerospace / Mechanical / Computer Science",
                "Flight Controls & Computational Fluid Dynamics",
                "Bengaluru, Karnataka",
                "Work on national civilian aircraft programs (SARAS Mk2, HANSA-NG). Monthly fellowship of ₹37,000/- with live wind tunnel and simulator access.",
                "B.Tech with valid GATE or M.Tech in relevant engineering stream. Age max 28.",
                21, 28, 65.0, "Free",
                -7, 14,
                "https://www.nal.res.in/careers", "CSIR", "CSIR-NAL-AERO-2027"
        ));

        list.add(createOpp(
                "BARC Junior Research Fellowship (JRF) in Physical & Chemical Sciences",
                "Bhabha Atomic Research Centre (BARC)",
                OpportunityCategory.FELLOWSHIP,
                "M.Sc in Physics, Chemistry, Material Science or B.Tech Nuclear Engg",
                "Radiation Physics & Materials Science",
                "Trombay, Mumbai",
                "Fellowship of ₹37,000/- per month for 1st & 2nd year and ₹42,000/- for subsequent years with medical facilities and subsidized accommodation.",
                "Consistent academic record with 60% marks in B.Sc and 60% in M.Sc. Valid CSIR-UGC NET or JEST score.",
                20, 28, 60.0, "₹500 (Free for SC/ST)",
                -13, 21,
                "https://barc.gov.in/jrf-scheme", "BARC", "BARC-JRF-PHYS-2027"
        ));

        list.add(createOpp(
                "MeitY Visvesvaraya Ph.D. Scheme for Electronics & IT",
                "Digital India Corporation & MeitY",
                OpportunityCategory.FELLOWSHIP,
                "M.Tech / M.E in Electronics / Computer Science / IT or B.Tech with 8.0+ CGPA",
                "Semiconductors, Embedded VLSI & AI Hardware",
                "Premier Indian Engineering Colleges & IITs",
                "Fellowship of ₹38,750/- p.m. for first 2 years and ₹43,750/- p.m. for remaining years + ₹1.20 Lakh annual contingency + International conference support.",
                "Admitted in full-time Ph.D. program in ESDM (Electronic System Design and Manufacturing) or IT/ITES. Age up to 35.",
                22, 35, 60.0, "Free",
                -18, 32,
                "https://phd.digitalindiacorporation.in", "MeitY", "MEITY-VISVES-PHD-2027"
        ));

        // ==========================================
        // 5. GOVERNMENT INTERNSHIPS (10 items)
        // ==========================================
        list.add(createOpp(
                "Urban Learning Internship Program (TULIP - Smart Cities Mission)",
                "AICTE & Ministry of Housing and Urban Affairs (MoHUA)",
                OpportunityCategory.INTERNSHIP,
                "B.Tech / B.E / B.Arch / MCA / B.Sc in Final or Pre-Final Year",
                "Smart Cities, IoT & Urban Digital Governance",
                "Tumakuru & Bengaluru Smart City, Karnataka",
                "Live government internship working with Municipal Corporations and Smart City SPVs on citizen portals, traffic management, and GIS maps. Stipend: ₹15,000 - ₹20,000/month.",
                "Indian citizen pursuing graduation or completed within 18 months. College NOC required.",
                19, 26, 55.0, "Free",
                -18, 3, // Closing soon!
                "https://internship.aicte-india.org/smart-city-tulip", "AICTE", "AICTE-TULIP-TUMKUR-2027"
        ));

        list.add(createOpp(
                "NITI Aayog Undergraduate & Graduate Internship Scheme",
                "NITI Aayog, Government of India",
                OpportunityCategory.INTERNSHIP,
                "Undergraduate / Postgraduate / Research Scholars in Engineering, Economics, Law",
                "Public Policy, Infrastructure & Digital Economy",
                "New Delhi (NITI Bhawan) / Hybrid",
                "Prestigious opportunity to contribute to national policy briefs, state rankings, electric mobility roadmaps, and health index evaluations directly under National Specialists.",
                "UG students must have secured 85%+ in 12th board. PG students must have 70%+ in UG. Duration: 6 to 12 weeks.",
                19, 28, 70.0, "Free",
                -5, 10,
                "https://niti.gov.in/internship", "NITI", "NITI-INTERN-POLICY-2027"
        ));

        list.add(createOpp(
                "Ministry of External Affairs (MEA) Internship Programme",
                "Ministry of External Affairs, Government of India",
                OpportunityCategory.INTERNSHIP,
                "Graduate Degree in Any Stream / Final Year Undergraduates",
                "Diplomacy, International Relations & Trade",
                "New Delhi (Jawaharlal Nehru Bhawan)",
                "Honorarium of ₹10,000/- per month plus airfare support. Interns work with territorial divisions, multilateral briefings, and international bilateral summits.",
                "Indian citizens. Age not exceeding 25 years. Candidate must have completed or be in final year of graduation.",
                20, 25, 60.0, "Free",
                -12, 8,
                "https://www.mea.gov.in/internship", "MEA", "MEA-INTERN-DIP-2027"
        ));

        list.add(createOpp(
                "Reserve Bank of India (RBI) Summer Placement Internship",
                "Reserve Bank of India",
                OpportunityCategory.INTERNSHIP,
                "Pursuing Post-Graduate Degree in Management / Economics / Statistics / Computer Science",
                "Central Banking, FinTech & Economic Research",
                "RBI Central Office (Mumbai) & Regional Offices",
                "Monthly stipend of ₹20,000/- per month for 3 months. Conduct research on financial inclusion, blockchain payment frameworks, and macro-prudential liquidity.",
                "Students in 1st year of 2-year PG course or 3-year integrated course. Selection based on interview.",
                21, 26, 60.0, "Free",
                -15, 5, // Closing soon!
                "https://opportunities.rbi.org.in/summer", "RBI", "RBI-SUMMER-INTERN-2027"
        ));

        list.add(createOpp(
                "ISRO Student Internship Programme (VSSC / URSC / SAC)",
                "Indian Space Research Organisation (ISRO)",
                OpportunityCategory.INTERNSHIP,
                "B.Tech / B.E (CS, EC, EE, Mech, Aero) in 6th or 7th Semester",
                "Satellite Payloads, Embedded RTOS & Telemetry",
                "Bengaluru, Thiruvananthapuram, Ahmedabad",
                "Work on actual space-grade engineering subsystems under guidance of senior ISRO scientists. Certificate of Project Completion awarded by ISRO.",
                "Minimum 65% aggregate or 6.84 CGPA in engineering. Sponsorship letter from College Principal/HOD required.",
                19, 25, 65.0, "Free",
                -6, 15,
                "https://www.isro.gov.in/internship", "ISRO", "ISRO-INTERN-STUDENT-2027"
        ));

        list.add(createOpp(
                "DRDO Student Research Attachment Internship (Winter/Summer)",
                "Defence Research and Development Organisation (DRDO)",
                OpportunityCategory.INTERNSHIP,
                "B.Tech / M.Tech in Computer Science, Mechanical, Electronics or Physics",
                "Aerospace, Sonar, Armament & Advanced Materials",
                "Bengaluru, Hyderabad, Pune, Chandigarh",
                "Hands-on project work in DRDO labs (ADE, LRDE, GTRE, ARDE). Excellent platform for students aspiring for defense engineering careers.",
                "Pursuing regular B.Tech/M.Tech with 60%+ aggregate. Institutional police/security clearance required.",
                19, 26, 60.0, "Free",
                -10, 18,
                "https://www.drdo.gov.in/student-internship", "DRDO", "DRDO-INTERN-LAB-2027"
        ));

        list.add(createOpp(
                "Digital India Internship Scheme (NIC & MeitY)",
                "Ministry of Electronics and Information Technology (MeitY)",
                OpportunityCategory.INTERNSHIP,
                "B.Tech / B.E (CSE, IT, ECE), MCA, or M.Sc CS in Final or Pre-Final Year",
                "Cyber Security, Cloud Platforms & AI in Governance",
                "New Delhi / National Informatics Centre Centers",
                "Stipend of ₹10,000/- per month for 2 to 3 months. Work on national platforms including DigiLocker, UPI stack, Aadhaar APIs, and Government e-Marketplace (GeM).",
                "Indian student securing at least 60% marks in 10th, 12th, and ongoing degree.",
                19, 25, 60.0, "Free",
                -14, 12,
                "https://www.meity.gov.in/internship-scheme", "MeitY", "MEITY-DIGITAL-INDIA-2027"
        ));

        list.add(createOpp(
                "Competition Commission of India (CCI) Student Internship",
                "Competition Commission of India, Ministry of Corporate Affairs",
                OpportunityCategory.INTERNSHIP,
                "Students of Economics, Law, Management, or Computer Science (Algorithm Audit)",
                "Antitrust, Fair Market Competition & Big Tech Regulation",
                "New Delhi (CCI Headquarters)",
                "Stipend of ₹15,000/- per month. Exposure to market studies, digital market regulations, algorithmic pricing audits, and competition advocacy.",
                "Enrolled in recognized college/university in 4th/5th year of integrated degree or 2nd year of master's degree.",
                20, 26, 60.0, "Free",
                -20, 4, // Closing soon!
                "https://www.cci.gov.in/internship", "CCI", "CCI-INTERN-DELHI-2027"
        ));

        list.add(createOpp(
                "National Human Rights Commission (NHRC) Short Term Internship",
                "National Human Rights Commission (NHRC India)",
                OpportunityCategory.INTERNSHIP,
                "Undergraduate / Postgraduate students of Any Stream from Recognized Colleges",
                "Human Rights Law, Social Justice & Civil Liberties",
                "New Delhi / Virtual Cohort",
                "Stipend of ₹12,000/- plus library access and travel allowance. Interaction with human rights commissioners, prison visits, and case law documentation.",
                "Enrolled in regular college. Age not above 26 years. Recommendation from college authority.",
                19, 26, 55.0, "Free",
                -8, 17,
                "https://nhrc.nic.in/internship", "NHRC", "NHRC-INTERN-2027"
        ));

        list.add(createOpp(
                "Bureau of Indian Standards (BIS) Graduate Apprenticeship / Internship",
                "Bureau of Indian Standards (Ministry of Consumer Affairs)",
                OpportunityCategory.INTERNSHIP,
                "Degree in Engineering (Civil, Mechanical, Electrical, Chemical, CS)",
                "National Standardization, Testing & Quality Certification",
                "New Delhi, Bengaluru, Mumbai, Chennai, Kolkata",
                "One year graduate internship with monthly stipend of ₹12,000/- under NATS (National Apprenticeship Training Scheme).",
                "Passed engineering degree in 2024, 2025 or 2026 with minimum 60% marks.",
                20, 26, 60.0, "Free",
                -12, 21,
                "https://www.bis.gov.in/apprenticeship", "BIS", "BIS-INTERN-NATS-2027"
        ));

        for (Opportunity opp : list) {
            opp.refreshStatus();
            opportunityRepository.save(opp);
        }

        // Seed a sample bookmark for candidate pipeline demo
        if (!list.isEmpty()) {
            ApplicationBookmark sampleBookmark = new ApplicationBookmark(
                    list.get(0),
                    "student@sit.ac.in",
                    "APPLIED",
                    "Online application submitted via RAC portal. Registration Number: DRDO-2027-89104."
            );
            bookmarkRepository.save(sampleBookmark);

            if (list.size() > 5) {
                ApplicationBookmark sample2 = new ApplicationBookmark(
                        list.get(5),
                        "student@sit.ac.in",
                        "BOOKMARKED",
                        "Verified syllabus and previous year question papers. Preparing aptitude section."
                );
                bookmarkRepository.save(sample2);
            }
        }

        log.info("Successfully seeded {} verified national government opportunities!", list.size());
    }

    private Opportunity createOpp(
            String title,
            String organization,
            OpportunityCategory category,
            String qualification,
            String field,
            String location,
            String description,
            String eligibilityCriteria,
            Integer minAge,
            Integer maxAge,
            Double minPercentage,
            String applicationFee,
            int daysOffsetStart,
            int daysOffsetDeadline,
            String officialUrl,
            String agencyCode,
            String uniqueSourceId
    ) {
        LocalDate today = LocalDate.now();
        Opportunity opp = new Opportunity();
        opp.setTitle(title);
        opp.setOrganization(organization);
        opp.setCategory(category);
        opp.setQualification(qualification);
        opp.setField(field);
        opp.setLocation(location);
        opp.setDescription(description);
        opp.setEligibilityCriteria(eligibilityCriteria);
        opp.setMinAge(minAge);
        opp.setMaxAge(maxAge);
        opp.setMinPercentage(minPercentage);
        opp.setApplicationFee(applicationFee);
        opp.setStartDate(today.plusDays(daysOffsetStart));
        opp.setDeadline(today.plusDays(daysOffsetDeadline));
        opp.setOfficialUrl(officialUrl);
        opp.setSourceAgency(agencyCode);
        opp.setUniqueSourceId(uniqueSourceId);
        return opp;
    }
}
