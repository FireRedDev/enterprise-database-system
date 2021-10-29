import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITargetSystemCredentials, TargetSystemCredentials } from '../target-system-credentials.model';
import { TargetSystemCredentialsService } from '../service/target-system-credentials.service';
import { ITargetSystem } from 'app/entities/target-system/target-system.model';
import { TargetSystemService } from 'app/entities/target-system/service/target-system.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

@Component({
  selector: 'jhi-target-system-credentials-update',
  templateUrl: './target-system-credentials-update.component.html',
})
export class TargetSystemCredentialsUpdateComponent implements OnInit {
  isSaving = false;

  targetSystemsCollection: ITargetSystem[] = [];
  employeesCollection: IEmployee[] = [];
  departmentsCollection: IDepartment[] = [];

  editForm = this.fb.group({
    id: [],
    username: [null, [Validators.required, Validators.minLength(6)]],
    password: [null, [Validators.required, Validators.minLength(6)]],
    targetSystem: [],
    employee: [],
    department: [],
  });

  constructor(
    protected targetSystemCredentialsService: TargetSystemCredentialsService,
    protected targetSystemService: TargetSystemService,
    protected employeeService: EmployeeService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ targetSystemCredentials }) => {
      this.updateForm(targetSystemCredentials);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const targetSystemCredentials = this.createFromForm();
    if (targetSystemCredentials.id !== undefined) {
      this.subscribeToSaveResponse(this.targetSystemCredentialsService.update(targetSystemCredentials));
    } else {
      this.subscribeToSaveResponse(this.targetSystemCredentialsService.create(targetSystemCredentials));
    }
  }

  trackTargetSystemById(index: number, item: ITargetSystem): number {
    return item.id!;
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  trackDepartmentById(index: number, item: IDepartment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITargetSystemCredentials>>): void {
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

  protected updateForm(targetSystemCredentials: ITargetSystemCredentials): void {
    this.editForm.patchValue({
      id: targetSystemCredentials.id,
      username: targetSystemCredentials.username,
      password: targetSystemCredentials.password,
      targetSystem: targetSystemCredentials.targetSystem,
      employee: targetSystemCredentials.employee,
      department: targetSystemCredentials.department,
    });

    this.targetSystemsCollection = this.targetSystemService.addTargetSystemToCollectionIfMissing(
      this.targetSystemsCollection,
      targetSystemCredentials.targetSystem
    );
    this.employeesCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesCollection,
      targetSystemCredentials.employee
    );
    this.departmentsCollection = this.departmentService.addDepartmentToCollectionIfMissing(
      this.departmentsCollection,
      targetSystemCredentials.department
    );
  }

  protected loadRelationshipsOptions(): void {
    this.targetSystemService
      .query({ 'targetSystemCredentialsId.specified': 'false' })
      .pipe(map((res: HttpResponse<ITargetSystem[]>) => res.body ?? []))
      .pipe(
        map((targetSystems: ITargetSystem[]) =>
          this.targetSystemService.addTargetSystemToCollectionIfMissing(targetSystems, this.editForm.get('targetSystem')!.value)
        )
      )
      .subscribe((targetSystems: ITargetSystem[]) => (this.targetSystemsCollection = targetSystems));

    this.employeeService
      .query({ 'targetSystemCredentialsId.specified': 'false' })
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesCollection = employees));

    this.departmentService
      .query({ 'targetSystemCredentialsId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing(departments, this.editForm.get('department')!.value)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsCollection = departments));
  }

  protected createFromForm(): ITargetSystemCredentials {
    return {
      ...new TargetSystemCredentials(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      targetSystem: this.editForm.get(['targetSystem'])!.value,
      employee: this.editForm.get(['employee'])!.value,
      department: this.editForm.get(['department'])!.value,
    };
  }
}
