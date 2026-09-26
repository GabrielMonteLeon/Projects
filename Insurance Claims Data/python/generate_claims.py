import random
from datetime import datetime, timedelta
from pathlib import Path

import pandas as pd
from faker import Faker


fake = Faker()

NUM_CLAIMS = 5000
OUTPUT_FILE = Path(__file__).parent.parent / "data" / "claims.csv"

STATES = [
    "NY", "NJ", "PA", "MA", "CT", "VA", "MD", "OH", "MI", "IL"
]

POLICY_TYPES = [
    "Auto", "Home", "Renters", "Life"
]

CLAIM_TYPES = {
    "Auto": ["Collision", "Theft", "Glass Damage", "Weather Damage"],
    "Home": ["Property Damage", "Theft", "Water Damage", "Fire"],
    "Renters": ["Theft", "Water Damage", "Fire", "Property Damage"],
    "Life": ["Life Benefit"]
}

STATUSES = ["Approved", "Denied", "Pending"]


def generate_claim():
    policy_type = random.choice(POLICY_TYPES)
    claim_type = random.choice(CLAIM_TYPES[policy_type])

    claim_date = fake.date_between(
        start_date="-365d",
        end_date="today"
    )

    reported_date = claim_date + timedelta(
        days=random.randint(0, 7)
    )

    claim_amount = round(
        random.uniform(500, 50000),
        2
    )

    claim_status = random.choices(
        STATUSES,
        weights=[70, 20, 10]
    )[0]

    if claim_status == "Approved":
        approved_amount = round(
            claim_amount * random.uniform(0.70, 1.00),
            2
        )
        processing_days = random.randint(2, 30)
    elif claim_status == "Denied":
        approved_amount = 0.00
        processing_days = random.randint(3, 45)
    else:
        approved_amount = 0.00
        processing_days = None

    fraud_flag = random.random() < 0.08

    return {
        "claim_id": None,
        "policy_id": f"POL{random.randint(100000, 999999)}",
        "customer_age": random.randint(18, 85),
        "state": random.choice(STATES),
        "policy_type": policy_type,
        "claim_type": claim_type,
        "claim_date": claim_date,
        "reported_date": reported_date,
        "claim_amount": claim_amount,
        "approved_amount": approved_amount,
        "claim_status": claim_status,
        "processing_days": processing_days,
        "fraud_flag": fraud_flag
    }


def main():
    claims = []

    for i in range(1, NUM_CLAIMS + 1):
        claim = generate_claim()
        claim["claim_id"] = f"CLM{i:06d}"
        claims.append(claim)

    df = pd.DataFrame(claims)

    OUTPUT_FILE.parent.mkdir(parents=True, exist_ok=True)
    df.to_csv(OUTPUT_FILE, index=False)

    print(f"Generated {len(df)} claims.")
    print(f"Saved to: {OUTPUT_FILE}")
    print("\nFirst 5 claims:")
    print(df.head())


if __name__ == "__main__":
    main()