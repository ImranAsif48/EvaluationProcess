# Evaluation Between Nanopub Retrieval Framework & Apache Lucene

To compare the two systems, a Java application was developed to measure the relevance of retrieved documents and the time taken to execute the search. First, we imported all nanopublications and IRIs data to Lucene Index-based search DB. Our 
Nanopublication retrieval framework used the MariaDb database (10.3.21-MariaDB MariaDB Server). We also applied the indexing approach for searching term in the database. Second, the Java application randomly chooses 1,000 search terms
from the 9,570,950 distinct labels associated with the IRIs that appear in the claims of the nanopublications.

### Prerequisite

- MySQL server (For importing the nanopub.sql)
- Java 8 or above

### Data
You can use this [link](https://drive.google.com/file/d/1gRurlnfHpU_9Io-JLDDNT7sGib1HnGqp/view?usp=sharing) to download the data. This data will be used for evaluating purpose.

### Usage

- Clone/Download this project and run from any java based editor 
  - Recommended Eclipse
- Configure the connection string with the MySQL database

### Procedure

1. The application automatically selected randomly 1000 topics from the available dataset. 
2. Create the Relevancy Dataset
3. Pass each topic to both systems to fetch the True Positive and False Positive based on the relevancy set.
4. In the end, it generates the .xlsx file as output
