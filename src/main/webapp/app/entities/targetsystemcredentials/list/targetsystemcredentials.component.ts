import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITargetsystemcredentials } from '../targetsystemcredentials.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { TargetsystemcredentialsService } from '../service/targetsystemcredentials.service';
import { TargetsystemcredentialsDeleteDialogComponent } from '../delete/targetsystemcredentials-delete-dialog.component';
import { MainComponent } from '../../../layouts/main/main.component';
import { ITargetsystem } from '../../targetsystem/targetsystem.model';
import { doc } from 'prettier';

@Component({
  selector: 'jhi-targetsystemcredentials',
  templateUrl: './targetsystemcredentials.component.html',
})
export class TargetsystemcredentialsComponent implements OnInit {
  targetsystemcredentials?: ITargetsystemcredentials[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected activeModal: NgbActiveModal,
    protected targetsystemcredentialsService: TargetsystemcredentialsService,
    protected newService: TargetsystemcredentialsService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected main: MainComponent
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.targetsystemcredentialsService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ITargetsystemcredentials[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  /*removeDouble(): void {
    let deleted = false;
    for (let i = 0; i < this.targetsystemcredentials!.length; i++) {
      this.targetsystemcredentials?.forEach(cred => {
        if (
          !deleted &&
          this.targetsystemcredentials![i].id !== cred.id &&
          this.targetsystemcredentials![i].systemuser?.id === cred.systemuser?.id
        ) {
          this.instanddelete(this.targetsystemcredentials![i].id);
          deleted = true;
        }
      });
    }
  }*/

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

  ngOnInit(): void {
    this.handleNavigation();
    this.main.showFooter = true;
    //this.removeDouble();
  }

  trackId(index: number, item: ITargetsystemcredentials): number {
    return item.id!;
  }

  delete(targetsystemcredentials: ITargetsystemcredentials): void {
    const modalRef = this.modalService.open(TargetsystemcredentialsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.targetsystemcredentials = targetsystemcredentials;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  instanddelete(id: number | undefined): void {
    if (id !== undefined) {
      this.targetsystemcredentialsService.delete(id).subscribe(() => {
        this.activeModal.close('deleted');
      });
    }
    this.loadPage();
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

  protected onSuccess(data: ITargetsystemcredentials[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/targetsystemcredentials'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.targetsystemcredentials = data ?? [];
    this.ngbPaginationPage = this.page;
    // this.removeDouble();
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
