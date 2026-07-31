import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerList } from './manager-home';

describe('ManagerList', () => {
  let component: ManagerList;
  let fixture: ComponentFixture<ManagerList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManagerList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagerList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
