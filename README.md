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
    * InfluxDB: http://influxdb.com/
    * Open TSDB: http://opentsdb.net/
    * Graphite: http://graphite.wikidot.com/






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