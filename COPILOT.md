# TaskFlow API — COPILOT.md

But de ce fichier
- Centraliser les consignes du projet
- Décrire l'architecture attendue
- Donner les commandes d'initialisation et d'exécution
- Servir de point d'évolution pour le projet lors des entretiens

---

**Projet**: TaskFlow API

**Objectif**: API REST sécurisée pour la gestion de tâches par utilisateur (CRUD, JWT, filtres, pagination).

**Stack imposée**
- Java 17
- Spring Boot (3.x de préférence)
- Spring Security
- Spring Data JPA
- JWT (bibliothèque au choix, ex: `jjwt` ou `java-jwt`)
- PostgreSQL en production, H2 en dev
- Maven
- Swagger / OpenAPI (springdoc-openapi)

**Architecture obligatoire**
- `controller` : endpoints REST (validation, mapping DTO)
- `service` : logique métier, transactions
- `repository` : interfaces JPA
- `entity` : entités JPA
- `dto` : objets d'entrée/sortie (request/response)
- `security` : configuration Spring Security, filtres JWT, gestion des utilisateurs

Structure de projet recommandée (package root = `com.taskflow`)
- com.taskflow
  - controller
  - service
  - repository
  - entity
  - dto
  - security
  - exception
  - config
  - util

Entités principales
- User
  - id (UUID)
  - username (String) unique
  - email (String) unique
  - password (String) hashed
  - roles (Set<Role>) — pour l'instant ROLE_USER par défaut
- Task
  - id (UUID)
  - title (String)
  - description (String)
  - status (Enum: TODO, IN_PROGRESS, DONE)
  - createdAt, updatedAt
  - owner (User) — relation ManyToOne

DTOs recommandés
- UserRegisterRequest, AuthRequest, AuthResponse (token)
- UserResponse (sans password)
- TaskCreateRequest, TaskUpdateRequest, TaskResponse

Sécurité & JWT (résumé)
- Endpoints publics: `/api/auth/register`, `/api/auth/login`, `/swagger-ui/**`, `/v3/api-docs/**`
- Endpoints protégés: tout le reste
- Lors de l'authentification: renvoyer un `accessToken` JWT avec claims minimum: `sub` (user id) et `username`.
- Un `JwtAuthenticationFilter` extrait le token, le valide, charge l'utilisateur et met l'Authentication dans le `SecurityContext`.
- Password hashing: `BCryptPasswordEncoder` (force: 10-12)
- Veiller à ne jamais exposer le mot de passe dans les DTO de sortie.

Règles métier importantes
- Chaque utilisateur ne peut voir/modifier/supprimer que ses propres tâches.
- Pagination et filtres sur `/api/tasks` :
  - params: `page`, `size`, `sort`, `status`, `q` (recherche texte dans title/description)
- Validation côté DTO (JSR-380 : `@NotBlank`, `@Size`, `@Email`)

Endpoints (exemples)
- POST `/api/auth/register` -> `UserRegisterRequest` -> 201
- POST `/api/auth/login` -> `AuthRequest` -> `AuthResponse{token}`
- GET `/api/users/me` -> infos user connecté
- CRUD tasks `/api/tasks`
  - GET `/api/tasks` (list, pageable, filter)
  - POST `/api/tasks` (create)
  - GET `/api/tasks/{id}` (must belong to user)
  - PUT `/api/tasks/{id}` (update)
  - DELETE `/api/tasks/{id}`

Swagger / OpenAPI
- Utiliser `springdoc-openapi-ui` pour exposer `/swagger-ui.html` ou `/swagger-ui/index.html`.
- Documenter les securitySchemes pour JWT (Bearer token dans Authorization header).

Bonnes pratiques & conseils pour l'entretien
- Structure claire : controller/service/repo — explique pourquoi (séparation des responsabilités).
- DTOs pour découpler l'API du modèle DB.
- Tests unitaires sur services et tests d'intégration pour controllers protégés.
- Exceptions gérées globalement (`@ControllerAdvice`) et réponses d'erreur standardisées.
- Logging structuré (pas d'informations sensibles) et gestion des erreurs JWT (expired, malformed).
- Explique les choix (ex: BCrypt vs Argon2, refresh tokens, choix des claims JWT).

Commandes utiles (PowerShell)
- Générer un projet Spring Boot via Start.Spring.io :
```powershell
Invoke-RestMethod -Uri "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.2.0&baseDir=taskflow&groupId=com.taskflow&artifactId=taskflow&name=taskflow&packageName=com.taskflow&javaVersion=17&dependencies=web,data-jpa,security,postgresql,validation,lombok" -OutFile taskflow.zip
Expand-Archive taskflow.zip -DestinationPath .\
Remove-Item taskflow.zip
```
- Lancer (H2/dev) :
```powershell
mvn spring-boot:run
```

Configuration recommandée pour `application.yml` (dev H2)
- datasource H2 en mémoire
- jwt : secret (en dev), expiration
- springdoc api docs enabled

Prochaine étape recommandée (ordre de travail pour l'entretien)
1. Initialiser le squelette Maven (Spring Initializr) — génération du projet.
2. Ajouter `pom.xml` dépendances manquantes (jjwt, springdoc-openapi).
3. Créer les entités `User` et `Task` + migrations (Flyway optional).
4. Repositories et DTOs.
5. Services (inscription, login, CRUD tasks).
6. Sécurité JWT et tests d'intégration.
7. Pagination, filtres, Swagger, Docker + PostgreSQL.

Notes pour toi
- Explique chaque morceau de code quand tu le commits (message clair).
- Si tu veux, je peux générer le projet Maven initial, puis implémenter les entités et la sécurité étape par étape.

Fichier source de référence
- Ce `COPILOT.md` doit rester à la racine du projet et évoluer avec les besoins.

---

Si tu veux, je peux maintenant :
- Option A : Initialiser le projet Maven avec Spring Initializr (je télécharge et décompresse le zip dans le workspace).
- Option B : Te guider pas-à-pas en te donnant les fichiers à créer (pom.xml minimal, structure, premières classes).

Dis-moi quelle option tu préfères ou valide que j'initialise directement le squelette Maven.
