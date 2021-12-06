import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavEntitiesComponent } from './nav-entities.component';

describe('NavEntitiesComponent', () => {
  let component: NavEntitiesComponent;
  let fixture: ComponentFixture<NavEntitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NavEntitiesComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NavEntitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
