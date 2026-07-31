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

interface Cart {
  userId: number;
  needIds: number[];
}

@Component({
  selector: 'app-cart',
  standalone: false,
  templateUrl: './cart.html',
  styleUrls: ['./cart.css'],
})
export class CartHome implements OnInit {
  cartNeeds: Need[] = [];
  userId: number = 0;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private auth: AuthService
  ) {}

  ngOnInit() {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    this.fetchCart();
  }

  fetchCart() {
  this.http.get<Cart>(`http://localhost:8080/cart/${this.userId}`).subscribe({
    next: (cart) => {
      if (cart.needIds.length === 0) {
        this.cartNeeds = [];
        this.cdr.detectChanges();
        return;
      }
      const fetches = cart.needIds.map(id =>
        this.http.get<Need>(`http://localhost:8080/cupboard/${id}`)
          .toPromise()
          .catch(() => null)
      );
      Promise.all(fetches).then(needs => {
        this.cartNeeds = needs.filter(n => n != null) as Need[];
        this.cdr.detectChanges();
      });
    },
    error: (err) => console.error('Error fetching cart:', err)
  });
}

  removeFromCart(need: Need) {
    this.http.delete(`http://localhost:8080/cart/${this.userId}/remove/${need.id}`).subscribe({
      next: () => this.fetchCart(),
      error: () => alert('Failed to remove item.')
    });
  }

  checkout() {
    this.http.post<number>(`http://localhost:8080/cart/${this.userId}/checkout`, {}).subscribe({
      next: (total) => {
        alert(`Checkout successful! Total: $${total.toFixed(2)}`);
        this.cartNeeds = [];
        this.cdr.detectChanges();
      },
      error: () => alert('Checkout failed.')
    });
  }

  goBack() {
    this.router.navigate(['/helper', this.userId]);
  }
}