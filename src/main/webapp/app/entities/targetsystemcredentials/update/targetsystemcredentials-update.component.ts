import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITargetsystemcredentials, Targetsystemcredentials } from '../targetsystemcredentials.model';
import { TargetsystemcredentialsService } from '../service/targetsystemcredentials.service';
import { ISystemuser } from 'app/entities/systemuser/systemuser.model';
import { SystemuserService } from 'app/entities/systemuser/service/systemuser.service';
import { ITargetsystem } from 'app/entities/targetsystem/targetsystem.model';
import { TargetsystemService } from 'app/entities/targetsystem/service/targetsystem.service';

@Component({
  selector: 'jhi-targetsystemcredentials-update',
  templateUrl: './targetsystemcredentials-update.component.html',
})
export class TargetsystemcredentialsUpdateComponent implements OnInit {
  isSaving = false;
  show = false;

  systemusersSharedCollection: ISystemuser[] = [];
  targetsystemsSharedCollection: ITargetsystem[] = [];

  editForm = this.fb.group({
    id: [],
    username: [
      null,
      [Validators.required, Validators.minLength(6), Validators.pattern('^(?=[a-zA-Z0-9._]{6,20}$)(?!.*[_.]{2})[^_.].*[^_.]$')],
    ],
    password: [
      null,
      [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&_])[A-Za-z\\d$@$!%*?&].{8,}'),
      ],
    ],
    systemuser: [],
    targetsystem: [],
  });

  constructor(
    protected targetsystemcredentialsService: TargetsystemcredentialsService,
    protected systemuserService: SystemuserService,
    protected targetsystemService: TargetsystemService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  password(): void {
    this.show = !this.show;
  }

  ngOnInit(): void {
    this.show = false;
    this.activatedRoute.data.subscribe(({ targetsystemcredentials }) => {
      this.updateForm(targetsystemcredentials);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const targetsystemcredentials = this.createFromForm();
    if (targetsystemcredentials.id !== undefined) {
      this.subscribeToSaveResponse(this.targetsystemcredentialsService.update(targetsystemcredentials));
    } else {
      this.subscribeToSaveResponse(this.targetsystemcredentialsService.create(targetsystemcredentials));
    }
  }

  trackSystemuserById(index: number, item: ISystemuser): number {
    return item.id!;
  }

  trackTargetsystemById(index: number, item: ITargetsystem): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITargetsystemcredentials>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(targetsystemcredentials: ITargetsystemcredentials): void {
    this.editForm.patchValue({
      id: targetsystemcredentials.id,
      username: targetsystemcredentials.username,
      password: targetsystemcredentials.password,
      systemuser: targetsystemcredentials.systemuser,
      targetsystem: targetsystemcredentials.targetsystem,
    });

    this.systemusersSharedCollection = this.systemuserService.addSystemuserToCollectionIfMissing(
      this.systemusersSharedCollection,
      targetsystemcredentials.systemuser
    );
    this.targetsystemsSharedCollection = this.targetsystemService.addTargetsystemToCollectionIfMissing(
      this.targetsystemsSharedCollection,
      targetsystemcredentials.targetsystem
    );
  }

  protected loadRelationshipsOptions(): void {
    this.systemuserService
      .query()
      .pipe(map((res: HttpResponse<ISystemuser[]>) => res.body ?? []))
      .pipe(
        map((systemusers: ISystemuser[]) =>
          this.systemuserService.addSystemuserToCollectionIfMissing(systemusers, this.editForm.get('systemuser')!.value)
        )
      )
      .subscribe((systemusers: ISystemuser[]) => (this.systemusersSharedCollection = systemusers));

    this.targetsystemService
      .query()
      .pipe(map((res: HttpResponse<ITargetsystem[]>) => res.body ?? []))
      .pipe(
        map((targetsystems: ITargetsystem[]) =>
          this.targetsystemService.addTargetsystemToCollectionIfMissing(targetsystems, this.editForm.get('targetsystem')!.value)
        )
      )
      .subscribe((targetsystems: ITargetsystem[]) => (this.targetsystemsSharedCollection = targetsystems));
  }

  protected createFromForm(): ITargetsystemcredentials {
    return {
      ...new Targetsystemcredentials(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      systemuser: this.editForm.get(['systemuser'])!.value,
      targetsystem: this.editForm.get(['targetsystem'])!.value,
    };
  }
}
