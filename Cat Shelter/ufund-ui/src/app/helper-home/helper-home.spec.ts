import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HelperHome } from './helper-home';

describe('HelperHome', () => {
  let component: HelperHome;
  let fixture: ComponentFixture<HelperHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HelperHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HelperHome);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
