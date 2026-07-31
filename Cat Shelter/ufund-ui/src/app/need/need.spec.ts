import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Need } from './need';

describe('Need', () => {
  let component: Need;
  let fixture: ComponentFixture<Need>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Need]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Need);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
