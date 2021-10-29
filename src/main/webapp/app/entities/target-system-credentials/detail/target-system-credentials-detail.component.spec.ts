import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TargetSystemCredentialsDetailComponent } from './target-system-credentials-detail.component';

describe('TargetSystemCredentials Management Detail Component', () => {
  let comp: TargetSystemCredentialsDetailComponent;
  let fixture: ComponentFixture<TargetSystemCredentialsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TargetSystemCredentialsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ targetSystemCredentials: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TargetSystemCredentialsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TargetSystemCredentialsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load targetSystemCredentials on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.targetSystemCredentials).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
