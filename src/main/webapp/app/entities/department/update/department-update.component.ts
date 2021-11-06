import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDepartment, Department } from '../department.model';
import { DepartmentService } from '../service/department.service';
import { ITargetsystem } from 'app/entities/targetsystem/targetsystem.model';
import { TargetsystemService } from 'app/entities/targetsystem/service/targetsystem.service';

@Component({
  selector: 'jhi-department-update',
  templateUrl: './department-update.component.html',
})
export class DepartmentUpdateComponent implements OnInit {
  isSaving = false;

  targetsystemsSharedCollection: ITargetsystem[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(6)]],
    targetsystems: [],
  });

  constructor(
    protected departmentService: DepartmentService,
    protected targetsystemService: TargetsystemService,
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

  trackTargetsystemById(index: number, item: ITargetsystem): number {
    return item.id!;
  }

  getSelectedTargetsystem(option: ITargetsystem, selectedVals?: ITargetsystem[]): ITargetsystem {
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
      targetsystems: department.targetsystems,
    });

    this.targetsystemsSharedCollection = this.targetsystemService.addTargetsystemToCollectionIfMissing(
      this.targetsystemsSharedCollection,
      ...(department.targetsystems ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.targetsystemService
      .query()
      .pipe(map((res: HttpResponse<ITargetsystem[]>) => res.body ?? []))
      .pipe(
        map((targetsystems: ITargetsystem[]) =>
          this.targetsystemService.addTargetsystemToCollectionIfMissing(targetsystems, ...(this.editForm.get('targetsystems')!.value ?? []))
        )
      )
      .subscribe((targetsystems: ITargetsystem[]) => (this.targetsystemsSharedCollection = targetsystems));
  }

  protected createFromForm(): IDepartment {
    return {
      ...new Department(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      targetsystems: this.editForm.get(['targetsystems'])!.value,
    };
  }
}
