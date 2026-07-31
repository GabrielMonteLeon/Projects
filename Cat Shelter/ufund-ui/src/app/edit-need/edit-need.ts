import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-edit-need',
  standalone: false,
  templateUrl: './edit-need.html',
  styleUrl: './edit-need.css',
})
export class EditNeed implements OnInit {
  needId: number = 0;
  userId: number = 0;

  name: string = '';
  quantity: number = 0;
  type: string = '';
  cost: number = 0;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private auth: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.needId = Number(this.route.snapshot.paramMap.get('id'));
    this.userId = Number(this.route.snapshot.paramMap.get('userId'));

    this.http.get<any>(`http://localhost:8080/cupboard/${this.needId}`).subscribe({
      next: (need) => {
        this.name = need.name;
        this.quantity = need.quantity;
        this.type = need.type;
        this.cost = need.cost;
        this.cdr.detectChanges();
      },
      error: () => alert('Failed to load need.')
    });
  }

  saveNeed() {
    if (!this.name || this.quantity == null || !this.type || this.cost == null) {
      alert('Please fill in all fields.');
      return;
    }

    const updated = {
      id: this.needId,
      name: this.name,
      quantity: this.quantity,
      type: this.type,
      cost: this.cost,
      userId: this.userId
    };

    this.http.put<any>('http://localhost:8080/cupboard', updated).subscribe({
      next: () => {
        alert('Need updated successfully!');
        this.router.navigate(['/manager', this.userId]);
      },
      error: () => alert('Failed to update need.')
    });
  }

  cancel() {
    this.router.navigate(['/manager', this.userId]);
  }
}