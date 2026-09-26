USE WAREHOUSE COMPUTE_WH;

SELECT
    'Row count is 5000' AS test_name,
    IFF(COUNT(*) = 5000, 'PASS', 'FAIL') AS result
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS

UNION ALL

SELECT
    'No duplicate claim IDs' AS test_name,
    IFF(COUNT(*) = 0, 'PASS', 'FAIL') AS result
FROM (
    SELECT claim_id
    FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
    GROUP BY claim_id
    HAVING COUNT(*) > 1
)

UNION ALL

SELECT
    'No null claim IDs' AS test_name,
    IFF(COUNT(*) = 0, 'PASS', 'FAIL') AS result
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
WHERE claim_id IS NULL

UNION ALL

SELECT
    'Claim amounts are positive' AS test_name,
    IFF(COUNT(*) = 0, 'PASS', 'FAIL') AS result
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
WHERE claim_amount <= 0

UNION ALL

SELECT
    'Approved amounts do not exceed claim amounts' AS test_name,
    IFF(COUNT(*) = 0, 'PASS', 'FAIL') AS result
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
WHERE approved_amount > claim_amount

UNION ALL

SELECT
    'Valid claim statuses' AS test_name,
    IFF(COUNT(*) = 0, 'PASS', 'FAIL') AS result
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
WHERE claim_status NOT IN ('Approved', 'Denied', 'Pending')

UNION ALL

SELECT
    'Valid customer ages' AS test_name,
    IFF(COUNT(*) = 0, 'PASS', 'FAIL') AS result
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
WHERE customer_age < 18 OR customer_age > 85

UNION ALL

SELECT
    'Processing days are non-negative' AS test_name,
    IFF(COUNT(*) = 0, 'PASS', 'FAIL') AS result
FROM INSURANCE_CLAIMS.ANALYTICS.CLAIMS
WHERE processing_days < 0;