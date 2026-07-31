import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

interface Need {
  id: number;
  name: string;
  quantity: number;
  type: string;
  cost: number;
}

@Component({
  selector: 'app-manager-home',
  standalone: false,
  templateUrl: './manager-home.html',
  styleUrls: ['./manager-home.css'],
})
export class ManagerList implements OnInit {
  needs: Need[] = [];
  userId: number = 0;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    this.fetchNeeds();
  }

  fetchNeeds() {
    this.http.get<Need[]>(`http://localhost:8080/cupboard/user/${this.userId}`).subscribe({
      next: (data) => {
        this.needs = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching needs:', err)
    });
  }

  goToNeed(need: Need) {
    this.router.navigate(['/need', need.id]);
  }

  editNeed(need: Need) {
    this.router.navigate(['/edit', need.id, this.userId]);
  }

  createNeed() {
    this.router.navigate(['/create', this.userId]);
  }

  deleteNeed(need: Need) {
    this.http.delete(`http://localhost:8080/cupboard/${need.id}`).subscribe({
      next: () => this.fetchNeeds(),
      error: (err) => console.error('Error deleting need:', err)
    });
  }
}