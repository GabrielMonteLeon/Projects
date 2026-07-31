import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NeedService } from './need.service';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-need',
  templateUrl: './need.html',
  styleUrl: '/need.css',
  standalone: false
})
export class Need implements OnInit {

  needId: number = 0;
  need: any;
  
  backButton(){
    const user = this.auth.getUser();
    if(user==null){
      this.router.navigate(['/login']);
    }
    else if (user.role === 'manager') {
      this.router.navigate(['/manager', user.id]);
    } 
    else if (user.role === 'helper') {
      this.router.navigate(['/helper', user.id]);
    }
  }

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private cdr: ChangeDetectorRef, 
    private router: Router,
    private auth: AuthService
  ) {}

  ngOnInit() {
    this.needId = Number(this.route.snapshot.paramMap.get('id'));

    this.needService.getNeed(this.needId).subscribe(data => {
      this.need = data;
      this.cdr.detectChanges();
    });
  }
}