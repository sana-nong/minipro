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

        if (opportunityRepository.count() == 0) {
            log.info("Seeding verified Government Opportunities for demonstration...");
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
                        "Central constitutional body authorized to conduct examinations for appointments to the Civil Services of the Union of India."
                ),
                new GovernmentSource(
                        "Staff Selection Commission",
                        "SSC",
                        "https://ssc.gov.in",
                        "https://ssc.gov.in/api/notices",
                        "REST_API",
                        "Government recruitment agency for Group B (Non-Gazetted) and Group C (Non-Technical) posts in central ministries."
                ),
                new GovernmentSource(
                        "All India Council for Technical Education",
                        "AICTE",
                        "https://www.aicte-india.org",
                        "https://internship.aicte-india.org",
                        "WEB_SCRAPER",
                        "National apex advisory body on technical education, offering Pragati scholarships and nationwide internship portal schemes."
                ),
                new GovernmentSource(
                        "National Scholarship Portal",
                        "NSP",
                        "https://scholarships.gov.in",
                        "https://scholarships.gov.in/schemes",
                        "WEB_SCRAPER",
                        "One-stop electronic scholarship gateway by Government of India managing central, state, and UGC/AICTE schemes."
                ),
                new GovernmentSource(
                        "Defence Research and Development Organisation",
                        "DRDO",
                        "https://rac.gov.in",
                        "https://rac.gov.in/drdo/feed",
                        "RSS_FEED",
                        "Premier defense R&D organisation offering Scientist 'B', Junior Research Fellowships, and apprentice roles."
                ),
                new GovernmentSource(
                        "Department of Science & Technology",
                        "DST",
                        "https://dst.gov.in",
                        "https://online-inspire.gov.in",
                        "WEB_SCRAPER",
                        "Nodal government department promoting scientific research, INSPIRE doctoral fellowships, and technology incubation."
                ),
                new GovernmentSource(
                        "Indian Space Research Organisation",
                        "ISRO",
                        "https://www.isro.gov.in",
                        "https://www.isro.gov.in/careers",
                        "WEB_SCRAPER",
                        "National space exploration agency providing apprenticeships, junior scientist posts, and student research attachments."
                )
        );

        for (GovernmentSource s : sources) {
            s.setLastSyncTime(LocalDateTime.now().minusHours(2));
            s.setSyncStatus("SUCCESS");
            s.setTotalOpportunitiesExtracted(2);
            sourceRepository.save(s);
        }
    }

    private void seedOpportunities() {
        LocalDate today = LocalDate.now();

        // 1. UPSC Civil Services
        Opportunity upscCSE = new Opportunity();
        upscCSE.setTitle("Civil Services (Preliminary) Examination 2027 (IAS, IPS, IFS)");
        upscCSE.setOrganization("Union Public Service Commission");
        upscCSE.setCategory(OpportunityCategory.COMPETITIVE_EXAM);
        upscCSE.setQualification("Bachelor's Degree in Any Discipline");
        upscCSE.setField("Civil Services & Administration");
        upscCSE.setLocation("All India (77 Test Cities)");
        upscCSE.setDescription("National competitive examination conducted for recruitment to higher civil services including IAS, IPS, IFS, IRS, and Central Group A services.");
        upscCSE.setEligibilityCriteria("Indian National. Age: 21 to 32 years. Candidate must hold a degree of any recognized university.");
        upscCSE.setMinAge(21);
        upscCSE.setMaxAge(32);
        upscCSE.setMinPercentage(50.0);
        upscCSE.setApplicationFee("₹100 (Female/SC/ST/Persons with Benchmark Disability exempt)");
        upscCSE.setStartDate(today.minusDays(10));
        upscCSE.setDeadline(today.plusDays(15));
        upscCSE.setOfficialUrl("https://upsc.gov.in/examinations/civil-services-preliminary-examination");
        upscCSE.setSourceAgency("UPSC");
        upscCSE.setUniqueSourceId("UPSC-CSE-PRE-2027");
        upscCSE.refreshStatus();
        opportunityRepository.save(upscCSE);

        // 2. DRDO Scientist B
        Opportunity drdoSci = new Opportunity();
        drdoSci.setTitle("Direct Recruitment of Scientist 'B' in DRDO (Computer Science & ECE)");
        drdoSci.setOrganization("Defence Research and Development Organisation (DRDO)");
        drdoSci.setCategory(OpportunityCategory.RECRUITMENT);
        drdoSci.setQualification("First Class B.Tech / B.E in CS / IT / Electronics");
        drdoSci.setField("Defense & Embedded Systems");
        drdoSci.setLocation("Bengaluru, Hyderabad, Pune, New Delhi");
        drdoSci.setDescription("Recruitment of Scientist 'B' (Level-10 Pay Matrix: ₹56,100/- plus allowances) across advanced defense laboratories.");
        drdoSci.setEligibilityCriteria("First Class Bachelor's Degree in Engineering with valid GATE score in CS/EC. Age: Max 28 years (General).");
        drdoSci.setMinAge(21);
        drdoSci.setMaxAge(28);
        drdoSci.setMinPercentage(60.0);
        drdoSci.setApplicationFee("₹100 (SC/ST/PwBD and Women candidates exempted)");
        drdoSci.setStartDate(today.minusDays(5));
        drdoSci.setDeadline(today.plusDays(20));
        drdoSci.setOfficialUrl("https://rac.gov.in/drdo/recruitment");
        drdoSci.setSourceAgency("DRDO");
        drdoSci.setUniqueSourceId("DRDO-RAC-SCB-CSE-2027");
        drdoSci.refreshStatus();
        opportunityRepository.save(drdoSci);

        // 3. AICTE Pragati
        Opportunity aictePragati = new Opportunity();
        aictePragati.setTitle("AICTE Pragati Scholarship Scheme for Girl Students (Degree/Diploma)");
        aictePragati.setOrganization("All India Council for Technical Education (AICTE)");
        aictePragati.setCategory(OpportunityCategory.SCHOLARSHIP);
        aictePragati.setQualification("First Year B.Tech / B.E / Polytechnic Diploma Student");
        aictePragati.setField("Technical & Higher Education");
        aictePragati.setLocation("All India");
        aictePragati.setDescription("Empowering young women in technical careers by providing ₹50,000 per annum towards college fee, books, and laptops.");
        aictePragati.setEligibilityCriteria("Only girl students admitted to AICTE approved technical colleges. Family annual income less than ₹8,00,000/-.");
        aictePragati.setMinAge(17);
        aictePragati.setMaxAge(24);
        aictePragati.setMinPercentage(60.0);
        aictePragati.setApplicationFee("Free (No application fee)");
        aictePragati.setStartDate(today.minusDays(14));
        aictePragati.setDeadline(today.plusDays(22));
        aictePragati.setOfficialUrl("https://www.aicte-india.org/schemes/students-development-schemes/pragati");
        aictePragati.setSourceAgency("AICTE");
        aictePragati.setUniqueSourceId("AICTE-PRAGATI-GIRLS-2027");
        aictePragati.refreshStatus();
        opportunityRepository.save(aictePragati);

        // 4. MoHUA & AICTE Smart Cities Internship (Closing soon - urgent filter demo)
        Opportunity aicteIntern = new Opportunity();
        aicteIntern.setTitle("Urban Planning and Digital Governance Internship (TULIP Scheme)");
        aicteIntern.setOrganization("AICTE & Ministry of Housing and Urban Affairs");
        aicteIntern.setCategory(OpportunityCategory.INTERNSHIP);
        aicteIntern.setQualification("B.Tech / B.E / B.Arch / MCA / B.Sc (Final / Pre-Final Year)");
        aicteIntern.setField("Smart Cities & Urban Tech");
        aicteIntern.setLocation("Tumakuru & Bengaluru Smart City, Karnataka");
        aicteIntern.setDescription("Hands-on 6-month live project internship with Municipal Corporations and Smart City SPVs. Monthly stipend of ₹15,000/-.");
        aicteIntern.setEligibilityCriteria("Students currently in B.Tech or graduated within 18 months. College NOC required.");
        aicteIntern.setMinAge(19);
        aicteIntern.setMaxAge(26);
        aicteIntern.setMinPercentage(55.0);
        aicteIntern.setApplicationFee("Free");
        aicteIntern.setStartDate(today.minusDays(18));
        aicteIntern.setDeadline(today.plusDays(3)); // 3 days left -> CLOSING_SOON!
        aicteIntern.setOfficialUrl("https://internship.aicte-india.org/smart-city-tulip");
        aicteIntern.setSourceAgency("AICTE");
        aicteIntern.setUniqueSourceId("AICTE-TULIP-TUMKUR-2027");
        aicteIntern.refreshStatus();
        opportunityRepository.save(aicteIntern);

        // 5. DST INSPIRE Fellowship
        Opportunity dstInspire = new Opportunity();
        dstInspire.setTitle("INSPIRE Fellowship for Doctoral Research in Basic & Applied Sciences");
        dstInspire.setOrganization("Department of Science & Technology (DST India)");
        dstInspire.setCategory(OpportunityCategory.FELLOWSHIP);
        dstInspire.setQualification("M.Sc / M.Tech / M.E with University 1st Rank or INSPIRE Scholar");
        dstInspire.setField("Scientific & Engineering Research");
        dstInspire.setLocation("All India (Universities, IISc, IITs, NITs)");
        dstInspire.setDescription("Financial fellowship of ₹37,000/- p.m. (JRF) and ₹42,000/- p.m. (SRF) plus HRA and ₹20,000/- annual contingency grant for 5 years.");
        dstInspire.setEligibilityCriteria("Indian citizens who secured 1st Rank in basic/applied science or engineering PG examination. Age 22-32 years.");
        dstInspire.setMinAge(22);
        dstInspire.setMaxAge(32);
        dstInspire.setMinPercentage(65.0);
        dstInspire.setApplicationFee("Free");
        dstInspire.setStartDate(today.minusDays(12));
        dstInspire.setDeadline(today.plusDays(25));
        dstInspire.setOfficialUrl("https://online-inspire.gov.in/fellowship");
        dstInspire.setSourceAgency("DST");
        dstInspire.setUniqueSourceId("DST-INSPIRE-DOC-2027");
        dstInspire.refreshStatus();
        opportunityRepository.save(dstInspire);

        // 6. SSC CGL
        Opportunity sscCgl = new Opportunity();
        sscCgl.setTitle("Staff Selection Commission Combined Graduate Level Examination (SSC CGL)");
        sscCgl.setOrganization("Staff Selection Commission");
        sscCgl.setCategory(OpportunityCategory.COMPETITIVE_EXAM);
        sscCgl.setQualification("Bachelor's Degree in Any Discipline from a Recognized University");
        sscCgl.setField("Central Government Administration & Audit");
        sscCgl.setLocation("All India");
        sscCgl.setDescription("Recruitment for Assistant Audit Officer, Assistant Section Officer (CSS, MEA, IB), Inspector of Income Tax, and Central Excise.");
        sscCgl.setEligibilityCriteria("Graduation in any field. Age between 18 and 30 years (relaxation for OBC/SC/ST).");
        sscCgl.setMinAge(18);
        sscCgl.setMaxAge(30);
        sscCgl.setMinPercentage(50.0);
        sscCgl.setApplicationFee("₹100 (Women/SC/ST/ESM exempt)");
        sscCgl.setStartDate(today.minusDays(8));
        sscCgl.setDeadline(today.plusDays(17));
        sscCgl.setOfficialUrl("https://ssc.gov.in/portal/cgl");
        sscCgl.setSourceAgency("SSC");
        sscCgl.setUniqueSourceId("SSC-CGL-EXAM-2027");
        sscCgl.refreshStatus();
        opportunityRepository.save(sscCgl);

        // 7. National Overseas Scholarship (NSP)
        Opportunity nspOverseas = new Opportunity();
        nspOverseas.setTitle("National Overseas Scholarship Scheme for Master's and Ph.D Abroad");
        nspOverseas.setOrganization("Ministry of Social Justice & Empowerment (NSP)");
        nspOverseas.setCategory(OpportunityCategory.SCHOLARSHIP);
        nspOverseas.setQualification("Bachelor's or Master's with minimum 60% marks");
        nspOverseas.setField("Global Higher Education & Research");
        nspOverseas.setLocation("International / Abroad Studies (USA, UK, Germany, Canada)");
        nspOverseas.setDescription("Full financial assistance including tuition fee, maintenance allowance ($15,400/yr), contingency allowance, visa fees, and air passage.");
        nspOverseas.setEligibilityCriteria("Age below 35 years. Total family income not exceeding ₹8 Lakh per annum. Candidate must have secured admission in top 500 QS ranked university.");
        nspOverseas.setMinAge(20);
        nspOverseas.setMaxAge(35);
        nspOverseas.setMinPercentage(60.0);
        nspOverseas.setApplicationFee("Free");
        nspOverseas.setStartDate(today.minusDays(15));
        nspOverseas.setDeadline(today.plusDays(5)); // Closing soon!
        nspOverseas.setOfficialUrl("https://scholarships.gov.in/nos-scheme");
        nspOverseas.setSourceAgency("NSP");
        nspOverseas.setUniqueSourceId("NSP-NOS-GLOBAL-2027");
        nspOverseas.refreshStatus();
        opportunityRepository.save(nspOverseas);

        // 8. ISRO Junior Research Fellowship
        Opportunity isroJrf = new Opportunity();
        isroJrf.setTitle("ISRO Satellite Centre (URSC) Junior Research Fellowship in Satellite Tech");
        isroJrf.setOrganization("Indian Space Research Organisation (ISRO)");
        isroJrf.setCategory(OpportunityCategory.FELLOWSHIP);
        isroJrf.setQualification("M.E / M.Tech in Electronics / Telecommunications / Computer Science");
        isroJrf.setField("Space Systems & Remote Sensing");
        isroJrf.setLocation("Bengaluru, Karnataka");
        isroJrf.setDescription("Research fellowship on next-generation satellite payload processing, optical communications, and on-board AI computing.");
        isroJrf.setEligibilityCriteria("First Class Master's degree in engineering or First Class B.Tech with valid GATE. Age max 28.");
        isroJrf.setMinAge(21);
        isroJrf.setMaxAge(28);
        isroJrf.setMinPercentage(65.0);
        isroJrf.setApplicationFee("Free");
        isroJrf.setStartDate(today.minusDays(5));
        isroJrf.setDeadline(today.plusDays(19));
        isroJrf.setOfficialUrl("https://www.isro.gov.in/ursc-jrf-recruitment");
        isroJrf.setSourceAgency("ISRO");
        isroJrf.setUniqueSourceId("ISRO-URSC-JRF-2027");
        isroJrf.refreshStatus();
        opportunityRepository.save(isroJrf);

        // Seed a sample bookmark for application tracker demo
        ApplicationBookmark sampleBookmark = new ApplicationBookmark(
                drdoSci,
                "student@sit.ac.in",
                "BOOKMARKED",
                "Preparing GATE CS revision notes. Documents verified."
        );
        bookmarkRepository.save(sampleBookmark);

        log.info("Successfully seeded 8 real-world government opportunities across all 5 categories!");
    }
}
