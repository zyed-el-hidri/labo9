# Laboratoire 9 - Test Containers

Tests d'intrégration pour couche de persistence.

Documentation: 
- Java : <https://java.testcontainers.org/>
- JUnit 5 : <https://java.testcontainers.org/quickstart/junit_5_quickstart/>

## Étapes

1. Commencez à partir du dernier laboratoire, avec une interface qui uniformise les fonctionalités d'accès aux données.
2. Créez une classe de tests abstraite afin de tester chaque implémentation de l'interface. Le but est d'avoir des tests 
centrés sur le comportement au lieu d'être centrés sur l'implémentation.
3. Implémentez cette classe de test pour votre persistence in-memory. Fournissez les méthodes manquantes et assurez-vous 
que tous les tests passent.
4. Implémentez cette classe de test pour votre persistence mongo. Cette ordre vous introduira au TDD (test-driven development), 
puisque vos tests sont déjà écrit mais pas l'implémentation.
   1. Utilisez la librairie `test-containers` afin de démarrer des instances Mongo aléatoires et conteneurisées. Utilisez 
   les valeurs du container généré afin de créer votre `Datastore` Morphia.
   2. 1 à 1, faites passer vos tests pour l'implémentation Mongo.
5. Répétez l'exercice de 0 afin de devenir de plus en plus à l'aise avec cette méthode!

Pour résumer toutes les étapes qu'on devrait faire lors de l'ajout d'une nouvelle implémentation (les noms sont seulement 
des suggestions, appliquée à la persistence mais valide pour tout type d'abstraction) :

1. Avoir une classe spécialisée pour la persistence qui est testée. Vous avez donc au moins une classe dans le style 
`CustomerPersistence` et un test `CustomerPeristenceITest` d'associé.
2. Extraire une interface pour l'implémentation unique actuelle. S'assurer que cette interface appartient à la couche 
logique et interagie seulement avec des objets de cette couche. Vous aurez donc maintenant la nouvelle interface 
`CustomerPersistence` et l'ancienne classe qui l'implémente, probablement renommée `CustomerPersistenceInMemory` et 
déplacé dans une nouvelle couche d'infrastructure/persistence/donnée.
3. Transformer les tests de l'implémentation en tests abstraits et génériques basés sur le comportement. Vous aurez 
donc maintenant la classe abstraite `CustomerPeristenceITest` qui définie les tests, et la classe `CustomerPeristenceInMemoryITest` 
qui l'extend (ne contient aucun tests, seulement les implémentation des méthodes abstraites).
4. Ajoutez une nouvelle implémentation vide de votre persistence, et ajoutez-y une classe de test correspondante qui 
implémente également la classe de tests abstraite. Vous aurez donc maintenant `CustomerPersistenceMongo` et `CustomerPersistenceMongoITest`.
5. Implémentez 1 à 1 les fonctionalités de `CustomerPersistenceMongo` afin de faire passer les tests!
