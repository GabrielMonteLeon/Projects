import sys
from pathlib import Path

import pandas as pd


INPUT_FILE = Path(__file__).parent.parent / "data" / "claims.csv"

REQUIRED_COLUMNS = [
    "claim_id",
    "policy_id",
    "customer_age",
    "state",
    "policy_type",
    "claim_type",
    "claim_date",
    "reported_date",
    "claim_amount",
    "approved_amount",
    "claim_status",
    "processing_days",
    "fraud_flag"
]

VALID_STATUSES = {"Approved", "Denied", "Pending"}


def validate_claims(df):
    errors = []

    if df.empty:
        errors.append("Dataset is empty.")

    missing_columns = [
        column for column in REQUIRED_COLUMNS
        if column not in df.columns
    ]

    if missing_columns:
        errors.append(f"Missing columns: {missing_columns}")

    if df["claim_id"].duplicated().any():
        errors.append("Duplicate claim IDs found.")

    if df["claim_id"].isna().any():
        errors.append("Missing claim IDs found.")

    if (df["claim_amount"] <= 0).any():
        errors.append("Claim amounts must be positive.")

    if (df["approved_amount"] < 0).any():
        errors.append("Approved amounts cannot be negative.")

    if (df["approved_amount"] > df["claim_amount"]).any():
        errors.append("Approved amounts cannot exceed claim amounts.")

    if (~df["claim_status"].isin(VALID_STATUSES)).any():
        errors.append("Invalid claim status found.")

    if (df["customer_age"] < 18).any() or (df["customer_age"] > 85).any():
        errors.append("Customer ages must be between 18 and 85.")

    if (df["processing_days"].dropna() < 0).any():
        errors.append("Processing days cannot be negative.")

    if errors:
        print("VALIDATION FAILED")

        for error in errors:
            print(f"- {error}")

        return False

    print("VALIDATION PASSED")
    print(f"Rows checked: {len(df)}")
    print(f"Columns checked: {len(df.columns)}")

    return True


def main():
    if not INPUT_FILE.exists():
        print(f"File not found: {INPUT_FILE}")
        sys.exit(1)

    df = pd.read_csv(INPUT_FILE)

    if not validate_claims(df):
        sys.exit(1)


if __name__ == "__main__":
    main()