import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-create-need',
  templateUrl: './create-need.html',
  styleUrl: './create-need.css',
  standalone: false 
})
export class CreateNeed {
  newNeed = {
    name: '',
    type: '',
    quantity: 1,
    cost: 0,
    userId: 0
  };

  private apiUrl = 'http://localhost:8080/cupboard';
  private userId: number = 0;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    this.newNeed.userId = this.userId;
  }

  onCreate() {
    this.http.post(this.apiUrl, this.newNeed).subscribe({
      next: (data: any) => {
        console.log('Need created successfully:', data);
        this.router.navigate(['/manager', this.userId]);
      },
      error: (err: any) => {
        console.error('Error creating need:', err);
        alert('Failed to create need. Check if your Backend is running!');
      }
    });
  }

  onBack() {
    this.router.navigate(['/manager', this.userId]);
  }
}