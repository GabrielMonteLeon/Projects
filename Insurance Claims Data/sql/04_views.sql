USE WAREHOUSE COMPUTE_WH;

CREATE OR REPLACE VIEW INSURANCE_CLAIMS.ANALYTICS.VW_CLAIM_VOLUME AS
SELECT
    DATE_TRUNC('month', claim_date) AS claim_month,
    COUNT(*) AS claim_count
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
GROUP BY claim_month;


CREATE OR REPLACE VIEW INSURANCE_CLAIMS.ANALYTICS.VW_CLAIM_METRICS AS
SELECT
    claim_type,
    COUNT(*) AS claim_count,
    ROUND(AVG(claim_amount), 2) AS average_claim_amount,
    ROUND(SUM(claim_amount), 2) AS total_claim_amount,
    ROUND(AVG(approved_amount), 2) AS average_approved_amount
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
GROUP BY claim_type;


CREATE OR REPLACE VIEW INSURANCE_CLAIMS.ANALYTICS.VW_PROCESSING_TIME AS
SELECT
    claim_type,
    COUNT(*) AS processed_claim_count,
    ROUND(AVG(processing_days), 2) AS average_processing_days,
    MIN(processing_days) AS fastest_processing_days,
    MAX(processing_days) AS slowest_processing_days
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
WHERE processing_days IS NOT NULL
GROUP BY claim_type;


CREATE OR REPLACE VIEW INSURANCE_CLAIMS.ANALYTICS.VW_CLAIM_OUTCOMES AS
SELECT
    claim_status,
    COUNT(*) AS claim_count,
    ROUND(
        COUNT(*) * 100.0 / SUM(COUNT(*)) OVER (),
        2
    ) AS outcome_percentage
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
GROUP BY claim_status;