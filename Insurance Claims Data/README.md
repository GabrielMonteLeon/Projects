Insurance Claims Data

A data pipeline that generates insurance claims data with Python, validates the data, and loads it into Snowflake for transformation and analysis.

The pipeline generates 5,000 claims with information such as policy type, claim type, claim amount, claim status, processing time, and fraud flags. The data is validated before being loaded into a Snowflake staging area and transformed into a final claims table.

Several Snowflake views are included to analyze claim volume, average claim amounts, processing times, and claim outcomes. SQL tests are also included to check the quality of the final data.

Built using Python, Pandas, Faker, SQL, and Snowflake.