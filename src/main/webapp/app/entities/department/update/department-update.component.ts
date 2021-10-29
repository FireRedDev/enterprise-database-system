import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDepartment, Department } from '../department.model';
import { DepartmentService } from '../service/department.service';
import { ITargetSystem } from 'app/entities/target-system/target-system.model';
import { TargetSystemService } from 'app/entities/target-system/service/target-system.service';

@Component({
  selector: 'jhi-department-update',
  templateUrl: './department-update.component.html',
})
export class DepartmentUpdateComponent implements OnInit {
  isSaving = false;

  targetSystemsSharedCollection: ITargetSystem[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    location: [],
    description: [],
    targetSystems: [],
  });

  constructor(
    protected departmentService: DepartmentService,
    protected targetSystemService: TargetSystemService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ department }) => {
      this.updateForm(department);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const department = this.createFromForm();
    if (department.id !== undefined) {
      this.subscribeToSaveResponse(this.departmentService.update(department));
    } else {
      this.subscribeToSaveResponse(this.departmentService.create(department));
    }
  }

  trackTargetSystemById(index: number, item: ITargetSystem): number {
    return item.id!;
  }

  getSelectedTargetSystem(option: ITargetSystem, selectedVals?: ITargetSystem[]): ITargetSystem {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartment>>): void {
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

  protected updateForm(department: IDepartment): void {
    this.editForm.patchValue({
      id: department.id,
      name: department.name,
      location: department.location,
      description: department.description,
      targetSystems: department.targetSystems,
    });

    this.targetSystemsSharedCollection = this.targetSystemService.addTargetSystemToCollectionIfMissing(
      this.targetSystemsSharedCollection,
      ...(department.targetSystems ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.targetSystemService
      .query()
      .pipe(map((res: HttpResponse<ITargetSystem[]>) => res.body ?? []))
      .pipe(
        map((targetSystems: ITargetSystem[]) =>
          this.targetSystemService.addTargetSystemToCollectionIfMissing(targetSystems, ...(this.editForm.get('targetSystems')!.value ?? []))
        )
      )
      .subscribe((targetSystems: ITargetSystem[]) => (this.targetSystemsSharedCollection = targetSystems));
  }

  protected createFromForm(): IDepartment {
    return {
      ...new Department(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      location: this.editForm.get(['location'])!.value,
      description: this.editForm.get(['description'])!.value,
      targetSystems: this.editForm.get(['targetSystems'])!.value,
    };
  }
}
