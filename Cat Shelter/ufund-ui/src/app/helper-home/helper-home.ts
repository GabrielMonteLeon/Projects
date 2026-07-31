import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../auth.service';

interface Need {
  id: number;
  name: string;
  quantity: number;
  type: string;
  cost: number;
}

@Component({
  selector: 'app-helper-home',
  standalone: false,
  templateUrl: './helper-home.html',
  styleUrls: ['./helper-home.css'],
})
export class HelperHome implements OnInit {
  needs: Need[] = [];
  filteredNeeds: Need[] = [];
  userId: number = 0;
  searchText: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private auth: AuthService
  ) {}

  ngOnInit() {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    this.fetchNeeds();
  }

  fetchNeeds() {
  this.http.get<Need[]>('http://localhost:8080/cupboard').subscribe({
    next: (data) => {
      this.needs = data;
      this.filteredNeeds = data;
      this.cdr.detectChanges();
    },
    error: (err) => console.error('Error fetching needs:', err)
  });
}


  onSearch() {
    const term = this.searchText.toLowerCase().trim();
    this.filteredNeeds = term
      ? this.needs.filter(n => n.name.toLowerCase().includes(term))
      : [...this.needs];
  }

  addToCart(need: Need) {
    this.http.post(`http://localhost:8080/cart/${this.userId}/add/${need.id}`, {}).subscribe({
      next: () => alert(`"${need.name}" added to cart!`),
      error: (err) => {
        if (err.status === 404) alert('Need no longer available.');
        else alert('Failed to add to cart.');
      }
    });
  }

  goToCart() {
    this.router.navigate(['/cart', this.userId]);
  }


}