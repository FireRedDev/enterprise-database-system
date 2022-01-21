import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITargetsystem, Targetsystem } from '../targetsystem.model';
import { TargetsystemService } from '../service/targetsystem.service';

@Component({
  selector: 'jhi-targetsystem-update',
  templateUrl: './targetsystem-update.component.html',
})
export class TargetsystemUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    type: [],
    url: [],
    password: [],
    username: [],
  });

  constructor(protected targetsystemService: TargetsystemService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ targetsystem }) => {
      this.updateForm(targetsystem);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const targetsystem = this.createFromForm();
    if (targetsystem.id !== undefined) {
      this.subscribeToSaveResponse(this.targetsystemService.update(targetsystem));
    } else {
      this.subscribeToSaveResponse(this.targetsystemService.create(targetsystem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITargetsystem>>): void {
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

  protected updateForm(targetsystem: ITargetsystem): void {
    this.editForm.patchValue({
      id: targetsystem.id,
      name: targetsystem.name,
      type: targetsystem.type,
      url: targetsystem.url,
      password: targetsystem.password,
      username: targetsystem.username,
    });
  }

  protected createFromForm(): ITargetsystem {
    return {
      ...new Targetsystem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      url: this.editForm.get(['url'])!.value,
      password: this.editForm.get(['password'])!.value,
      username: this.editForm.get(['username'])!.value,
    };
  }
}
