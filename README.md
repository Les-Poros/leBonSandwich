# leBonSandwich


Membres du projet :
- Maeva Butaye    ( Lilychaan )
- Camille Schwarz ( S-Camille )
- Léo Galassi     ( ElGitariste )
- Quentin Rimet   ( QuentinRimet )

# Prérequis :

* Maven
* Docker
* Docker-compose

## Pour lancer le service categoriesandwich

- mvn clean install -DskipTests
- docker-compose up --build -d

Celui ci sera alors accessible sur le port 8082 de votre adresse docker

## Pour lancer le service commandes

- mvn clean install -DskipTests
- docker-compose up --build -d

Celui ci sera alors accessible sur le port 8083 de votre adresse docker

## Routes Api

-	Obtenir la description d'une catégorie
  GET : /categories/{id}
-	Obtenir la liste des catégories de sandwich
  GET : /categories
-	obtenir la liste des sandwichs d'une catégorie
  GET : /categories/{id}/sandwichs
-	Obtenir la description d'un sandwich
  GET : /sandwichs/{id}
- Obtenir la liste des sandwichs du catalogue
  GET : /sandwichs
-	Obtenir la liste des sandwich avec filtrage sur le type de pain et/ou tarif max
  GET : /sandwichs?type=val&tarif=val2
-	Obtenir la liste des sandwichs avec pagination
  GET : /sandwichs?page=val&limit=val2
-	Creer une commande
  POST : /commandes
-	Modifier une commande
  PUT : /commandes/{id}
-	détail de la commande
  GET : /commandes/{id}
-	liste des commandes
  GET : /commandes
