# demo-jmx #

Ce projet contient les sources pour la démo JMX.

## Pourquoi faire des métriques ##

Cette présentation concerne la prise de mesure dans les applications.

Souvent sur les applications, on se concentre sur la réalisation des fonctionnalités métiers, et on a tendance à faire
passer en priorité moindre les outils utiles pour l'opérationnel (les logs, etc...)

Les métriques font parties de cette catégories.

Et quand on arrive en production, on est incapable de savoir comment se comporte réellement l'application.

### quelques questions qu'on peut se poser ###

* Est-ce lent ?
* Combien as-t'on de client qui utilise l'application ?
* Quels sont les fonctionnalités les plus utilisées ?
* Qu'est-ce qui prend du temps ?

### Aller plus loin ###

Les metriques ont une valeur business: http://pivotallabs.com/139-metrics-metrics-everywhere/

## 3 grands principes ##

1. Collecter,
2. Stocker,
3. Analyser / présenter.

## Métriques "système" ##

Les applications VSCt en production sont déjà monitorées:
* monitoring système
* monitoring réseau (apache, haproxy)

=> tout ceci est fourni par la DT.

## Métriques "fonctionnelles" ##

### Moyens de Collectes ###

* logs:

    private static final Logger LOGGER = LoggerFactory.getLogger("audit.service1");
    ...
    public void service1() {
        LOGGER.info("Another Call to Service1");
    }

* Base de données:

    public void service1() {
        database.executeSql("Insert into T_AUDIT values ('Service1','Called')");
    }

* Fichiers à plat avec framework fait maison:

    class Counter {
       private long counterValue = 0;
       private long lastTimeStamp;
       public void inc() {
          if (System.currentTimeMillis() > lastTimeStamp + 1000 * 3600) {
             saveToFileEachHour(counterValue);
             counterValue = 0;
          }
          counterValue++;
       }
    }

    public void service1() {
       Counter serviceCounter = getCounter("service1");
       serviceCounter.inc();
    }

* Jmx => on va en reparler.

### Stockage ###

* Fichiers de logs
* Base de données
* Fichiers à plat fait maison
* TSDB: Time Series DataBase
    * InfluxDB: [http://influxdb.com/](http://influxdb.com/)
    * Open TSDB: [http://opentsdb.net/](http://opentsdb.net/)
    * **Graphite**: [http://graphite.wikidot.com/](http://graphite.wikidot.com/) => mis à dispo par la DT.

### Analyse / Visualization ###

* Fichiers de logs: grep -c "service1"
* Base de données: select count(1) from T_AUDIT where service_name = 'service1';
* Fichiers à plat fait maison:
* TSDB: possibiliter de plugger des outils compatibles:
    * [graphene](http://jondot.github.io/graphene/)
    * [grafana](http://grafana.org/)
    * plein plein plein d'autres... voir [ici](http://graphite.readthedocs.org/en/latest/tools.html)

## Focus sur JMX ##

C'est ce qui nous interesse en tant que développeur.

### Principes de JMX ###

* Des objets (méthodes, attributs) nommés et identifiables de manière unique: les MBeans (pour Managed Beans)
* Une organisation arborescente des mbeans
* Un protocole intégrant le rafraichissement automatique des informations.
* Des outils standard JMV pour visualiser les mbeans: [DEMO !]
    * JConsole: vieux mais toujours vaillant
    * JVirtualVM: plus pro
    * Java Mission Control: Plus hype et récent mais buggé.

### Mise en place ###

* En fait c'est super compliqué à mettre en oeuvre
* Il faut faire des descripteurs pour chaque méthode / attribut exposé

### Spring à la rescousse ###

* Simplification de la publication Bean spring en tant que MBean
* Demo.

=> scenario:
un site web vend des trucs à des clients:
une base de produit, une base de client.
Cas d'utilisation:
un client se connecte (login), recherche des produits (selectByPrice), en choisi quelqu'un (addToBasket) puis passe commande (order).





It contains the following:
* a server that publish webservices
* a client that is used to generate load on the server

=> quicky jmx:
* interêt des métriques : http://pivotallabs.com/139-metrics-metrics-everywhere/
* 3 parties:
- prise de mesures
- stockages
- analyse / rendu
* Prise de mesure:
- plusieurs moyens: analyse de fichiers de logs (grep -c), select count en base de données, jmx
- jmx
* Principes de JMX
- Nommage
- Protocole
- Jmx est compliqué
* Utilisation avec Spring
-
(* outils pratiques:
- Jolokia: expose jmx en rest
- Hawtio: console d'admin client jolokia)
* Cas d'utilisation:
-

### Quicky Steps ###

## Level 0 ##

* Ici, pas du tout de métrique,
* Présentation du client de test, du code du serveur avec aucune instrumentation
* Déroule un premier scenario.
* Lance le client qui charge
* On a quand même certaine metriques: Montre la JConsole, on voit la mémoire, le cpu, les threads qui bougent.

## Level 1 ##

* Publication du FrontEndServices comme MBean
* Implémentation d'un compteur basique: exemple: comptage du nombre d'order.

## Level 2 ##

* Utilisation de metrics pour mesurer des choses plus complexes comme les temps de réponses
* Mesure du temps de réponse de l'order.
