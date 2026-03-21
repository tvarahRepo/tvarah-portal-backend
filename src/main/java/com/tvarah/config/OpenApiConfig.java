package com.tvarah.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        private static final String OAUTH2_SCHEME = "keycloak_oauth2";

        @Value("${keycloak.token-uri}")
        private String tokenUrl;

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Tvarah Internal API Documentation")
                                                .description("""
                                                                ## What is Tvarah?

                                                                **Tvarah** was founded on a simple observation: hiring for AI and Data roles demands far greater rigor, domain depth, and expertise than traditional recruitment models provide.\s
                                                                Built by professionals with vast leadership experience in AI, data, and business strategy, Tvarah was created to bring clarity, accountability, and long-term thinking back into technical hiring.

                                                                This platform is the backend engine powering that mission — a production-grade talent intelligence and recruitment operations system.\s
                                                                It drives the full hiring lifecycle — from sourcing and scoring candidates to scheduling interviews and tracking offers —\s
                                                                giving HR teams, recruiters, and hiring managers a single, structured backend to operate from.

                                                                ---

                                                                ## Business Capabilities

                                                                - **Client Management** — Onboard and manage hiring companies segmented by industry, size, and assigned recruiter.
                                                                - **Job Description Engine** — Create structured JDs with skill arrays (required / must-have / good-to-have), experience bands, salary ranges, round configuration, and non-negotiable rules.
                                                                - **Candidate Pool** — Maintain rich candidate profiles covering identity, work history, education, skills, documents, social presence, CTC preferences, and notice period details.
                                                                - **Intelligent Scoring** — Multi-dimensional scoring engine evaluating candidates across education quality, experience depth, skill breadth, domain alignment, and fraud risk signals.
                                                                - **Application Pipeline** — Link candidates to job openings, manage pipeline stages, and generate per-application JD-fit evaluations (skill match, domain match, experience relevance).
                                                                - **Interview Management** — Schedule multi-round interviews with interviewer details, mode, focus areas, questions, feedback, and round-level scores.
                                                                - **Master Data** — Centralized reference tables for industry, department, job title, designation, skill, institution, degree, and specialization — ensuring consistency across all records.

                                                                ---

                                                                ## Backend Tech Stack

                                                                `Java 17` · `Spring Boot 3.2` · `Spring Data JPA` · `PostgreSQL 16` · `Liquibase` · `Keycloak 24` · `Docker`

                                                                ---

                                                                ## Architecture

                                                                Tvarah follows a **enterprise production-grade layered architecture** with strict layer boundaries:

                                                                ```
                                                                              🟣 User  ◄──────────────────── ( External Client, FE, ML-Ops, Swagger UI)
                                                                                  │
                                                                                  │  HTTP + Bearer JWT
                                                                                  ▼
                                                                ┌─────── 🟣 Security Filter Chain ───────┐
                                                                │   Bearer Token Authentication Filter  │
                                                                │   JWT Signature + Expiry Validation   │◄── Keycloak
                                                                │   JwtAuthConverter: Role Extraction   │
                                                                │   (Realm Access + Resource Access)    │
                                                                └──────────────────┬────────────────────┘
                                                                                   │
                                                                                   ▼
                                                                ┌───── 🟣 Authorization Interceptor ────┐
                                                                │       Claim presence validation       │
                                                                │       Token expiry double-check       │
                                                                │       User context population         │
                                                                │       Request attribute injection     │
                                                                └──────────────────┬────────────────────┘
                                                                                   │
                                                                                   ▼
                                                                ┌────────── 🟣 Controller ──────────────┐
                                                                │   REST endpoints + request validation │
                                                                │   @PreAuthorize RBAC enforcement      │
                                                                └──────────────────┬────────────────────┘
                                                                                   │
                                                                                   ▼
                                                                ┌─────────── 🟣 Service ────────────────┐
                                                                │   Business logic + orchestration      │
                                                                └──────────────────┬────────────────────┘
                                                                                   │
                                                                                   ▼
                                                                ┌────────── 🟣 Repository ──────────────┐
                                                                │   Spring Data JPA → PostgreSQL        │
                                                                │   Schema versioned via Liquibase      │◄── PostgreSQL
                                                                └───────────────────────────────────────┘
                                                                ```

                                                                ---

                                                                ## Authentication

                                                                All endpoints require a valid **Bearer JWT** issued by Keycloak.\s
                                                                Use the **Authorize** button below, enter your **Client ID** and **Client Secret**, and the token will be applied automatically to every request.
                                                                """)
                                                .version("0.0.1")
                                                .contact(new Contact()
                                                                .name("Tvarah Team")
                                                                .email("uday.matta@tvarah.com")))
                                .addTagsItem(new Tag().name("Candidate Management"))
                                .addTagsItem(new Tag().name("Client Management"))
                                .addTagsItem(new Tag().name("JD Management"))
                                .addSecurityItem(new SecurityRequirement().addList(OAUTH2_SCHEME))
                                .components(new Components()
                                                .addSecuritySchemes(OAUTH2_SCHEME, new SecurityScheme()
                                                                .type(SecurityScheme.Type.OAUTH2)
                                                                .description("Keycloak authentication — enter your Client ID and Client Secret to obtain an access token.")
                                                                .flows(new OAuthFlows()
                                                                                .clientCredentials(new OAuthFlow()
                                                                                                .tokenUrl(tokenUrl)))));
        }
}
