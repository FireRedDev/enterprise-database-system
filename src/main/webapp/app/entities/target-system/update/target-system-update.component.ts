import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITargetSystem, TargetSystem } from '../target-system.model';
import { TargetSystemService } from '../service/target-system.service';

@Component({
  selector: 'jhi-target-system-update',
  templateUrl: './target-system-update.component.html',
})
export class TargetSystemUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected targetSystemService: TargetSystemService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ targetSystem }) => {
      this.updateForm(targetSystem);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const targetSystem = this.createFromForm();
    if (targetSystem.id !== undefined) {
      this.subscribeToSaveResponse(this.targetSystemService.update(targetSystem));
    } else {
      this.subscribeToSaveResponse(this.targetSystemService.create(targetSystem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITargetSystem>>): void {
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

  protected updateForm(targetSystem: ITargetSystem): void {
    this.editForm.patchValue({
      id: targetSystem.id,
      name: targetSystem.name,
    });
  }

  protected createFromForm(): ITargetSystem {
    return {
      ...new TargetSystem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
