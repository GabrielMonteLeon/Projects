USE WAREHOUSE COMPUTE_WH;

CREATE OR REPLACE TABLE INSURANCE_CLAIMS.ANALYTICS.CLAIMS AS
SELECT
    claim_id,
    policy_id,
    customer_age,
    UPPER(TRIM(state)) AS state,
    INITCAP(TRIM(policy_type)) AS policy_type,
    TRIM(claim_type) AS claim_type,
    claim_date,
    reported_date,
    claim_amount,
    approved_amount,
    claim_status,
    processing_days,
    fraud_flag,
    DATEDIFF('day', claim_date, reported_date) AS days_to_report,
    CASE
        WHEN claim_amount < 5000 THEN 'Low'
        WHEN claim_amount <= 20000 THEN 'Medium'
        ELSE 'High'
    END AS claim_amount_category,
    ROUND(
        approved_amount / NULLIF(claim_amount, 0) * 100,
        2
    ) AS approval_percentage
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS_RAW;