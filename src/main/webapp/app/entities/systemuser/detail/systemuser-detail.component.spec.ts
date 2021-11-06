import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SystemuserDetailComponent } from './systemuser-detail.component';

describe('Systemuser Management Detail Component', () => {
  let comp: SystemuserDetailComponent;
  let fixture: ComponentFixture<SystemuserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SystemuserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ systemuser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SystemuserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SystemuserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load systemuser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.systemuser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
