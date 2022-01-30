import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITargetsystem } from '../targetsystem.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { TargetsystemService } from '../service/targetsystem.service';
import { TargetsystemDeleteDialogComponent } from '../delete/targetsystem-delete-dialog.component';
import { MainComponent } from '../../../layouts/main/main.component';
import { DepartmentComponent } from '../../department/list/department.component';
import { IDepartment } from '../../department/department.model';

@Component({
  selector: 'jhi-targetsystem',
  templateUrl: './targetsystem.component.html',
})
export class TargetsystemComponent implements OnInit {
  targetsystems?: ITargetsystem[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  departments?: IDepartment[];
  ngbPaginationPage = 1;

  constructor(
    protected targetsystemService: TargetsystemService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected main: MainComponent
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.targetsystemService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ITargetsystem[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.main.showFooter = true;
  }

  trackId(index: number, item: ITargetsystem): number {
    return item.id!;
  }

  delete(targetsystem: ITargetsystem): void {
    const modalRef = this.modalService.open(TargetsystemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.targetsystem = targetsystem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  downloadCsv(targetsystem: ITargetsystem): void {
    location.href =
      'http://localhost:8080/api/targetsystemcredentials/csv/' + targetsystem.id!.toString() + '/' + targetsystem.csvAttributes!.toString();
  }
  downloadXML(targetsystem: ITargetsystem): void {
    location.href = 'http://localhost:8080/api/targetsystemcredentials/xml/' + targetsystem.id!.toString();
  }
  downloadJSON(targetsystem: ITargetsystem): void {
    location.href = 'http://localhost:8080/api/targetsystemcredentials/json/' + targetsystem.id!.toString();
  }
  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ITargetsystem[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/targetsystem'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.targetsystems = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
