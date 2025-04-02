# Application de Test MongoDB pour le Laboratoire 9

Cette application expose une API REST pour tester votre implémentation MongoDB de `CustomerPersistence`. Elle vous permet d'interagir facilement avec la base de données MongoDB via des endpoints HTTP, ce qui est idéal pour apprendre et tester.

## Structure du projet

L'application respecte exactement la structure de packages de votre projet:

```
ca.ulaval.glo2003
├── api (nouvelle)
│   ├── CustomerResource.java
│   ├── dtos
│   │   ├── AddEmailRequest.java
│   │   ├── CreateCustomerRequest.java
│   │   ├── CustomerDto.java
│   │   └── UpdateCustomerRequest.java
│   └── exceptionmappers
│       ├── GenericExceptionMapper.java
│       └── IllegalArgumentExceptionMapper.java
├── customer
│   ├── infra
│   │   ├── CustomerMongo.java (existant)
│   │   ├── CustomerPersistenceInMemory.java (existant)
│   │   └── CustomerPersistenceMongo.java (existant)
│   └── logic
│       ├── Customer.java (existant)
│       ├── CustomerPersistence.java (existant)
│       ├── CustomerService.java (nouveau)
│       ├── Email.java (existant)
│       └── EmailType.java (existant)
├── shared
│   └── infra
│       └── DatastoreProvider.java (existant)
└── Main.java (nouveau)
```

## Prérequis

- Java 11 ou supérieur
- Maven
- MongoDB (via Docker dans `docker-compose.yaml`)
- Insomnia (ou un autre client REST comme Postman)

## Configuration et démarrage

1. **Démarrer MongoDB**

   ```bash
   docker-compose up -d
   ```

2. **Ajouter les dépendances**

   Ajoutez les dépendances listées dans le fichier `pom-dependencies.xml` à votre fichier `pom.xml`.

3. **Compiler et exécuter**

   ```bash
   mvn clean package
   java -jar target/labo9-1.0-SNAPSHOT.jar
   ```

4. **Importer la collection Insomnia**

   Importez le fichier `insomnia-collection.json` dans Insomnia pour tester facilement les endpoints.

## Endpoints API

- `GET /api/customers` - Récupérer tous les clients
- `GET /api/customers/{id}` - Récupérer un client par ID
- `POST /api/customers` - Créer un nouveau client
- `PUT /api/customers/{id}` - Mettre à jour un client existant
- `POST /api/customers/{id}/emails` - Ajouter un email à un client

## Exemple d'utilisation avec Insomnia

1. **Créer un client**

   ```json
   POST /api/customers
   {
     "name": "John Doe",
     "birthDate": "1990-01-01",
     "emails": {
       "work": ["john.doe@company.com"],
       "personal": ["john@personal.com"]
     }
   }
   ```

2. **Récupérer l'ID du client créé et le définir comme variable dans Insomnia**
   
   Après avoir créé un client, copiez l'ID retourné dans la réponse et définissez-le comme valeur de la variable `customer_id` dans l'environnement Insomnia.

3. **Ajouter un email**

   ```json
   POST /api/customers/{id}/emails
   {
     "type": "work",
     "email": "john.doe.new@company.com"
   }
   ```

## Principes de conception appliqués

Cette solution suit plusieurs principes de conception importants:

### Principes SOLID

1. **Single Responsibility Principle (SRP)**:
   - `CustomerResource` gère uniquement les requêtes HTTP
   - `CustomerService` contient uniquement la logique métier
   - `CustomerPersistenceMongo` s'occupe uniquement de l'interaction avec MongoDB

2. **Open/Closed Principle (OCP)**:
   - Le code est ouvert à l'extension mais fermé à la modification
   - Vous pouvez facilement ajouter de nouvelles implémentations de `CustomerPersistence`

3. **Liskov Substitution Principle (LSP)**:
   - Toutes les implémentations de `CustomerPersistence` peuvent être utilisées de manière interchangeable
   - Le système ne se soucie pas de l'implémentation concrète utilisée

4. **Interface Segregation Principle (ISP)**:
   - Les interfaces sont petites et spécifiques
   - `CustomerPersistence` expose seulement les méthodes nécessaires

5. **Dependency Inversion Principle (DIP)**:
   - Les classes de haut niveau dépendent des abstractions, pas des détails
   - `CustomerService` dépend de l'interface `CustomerPersistence`, pas de `CustomerPersistenceMongo`

### Architecture en couches

L'application est organisée en trois couches distinctes:

1. **Couche API (Resources et DTOs)**:
   - Gère la conversion entre les requêtes/réponses HTTP et les objets du domaine
   - S'occupe du routage des requêtes vers les services appropriés

2. **Couche Service**:
   - Contient la logique métier
   - Coordonne les opérations entre les différentes parties du système

3. **Couche Persistance**:
   - Gère le stockage et la récupération des données
   - Abstrait les détails spécifiques de la base de données

## Apprendre MongoDB avec cette application

Cette application est idéale pour apprendre MongoDB:

1. **Observer les requêtes MongoDB**: Utilisez MongoDB Compass pour voir les documents stockés et les requêtes exécutées.

2. **Comprendre les conversions**: Observez comment les objets Java sont convertis en documents MongoDB et vice versa.

3. **Tester différents scénarios**: Utilisez Insomnia pour créer, récupérer et mettre à jour des clients, puis observez comment les données changent dans MongoDB.

4. **Explorer les fonctionnalités de Morphia**: Examinez comment Morphia (l'ODM utilisé) facilite le mapping entre Java et MongoDB.

## Conclusion

Cette application REST vous fournit une façon interactive de tester et d'apprendre l'implémentation MongoDB de `CustomerPersistence`. Elle suit les bonnes pratiques de développement et vous offre une expérience pratique avec MongoDB dans un contexte réel.