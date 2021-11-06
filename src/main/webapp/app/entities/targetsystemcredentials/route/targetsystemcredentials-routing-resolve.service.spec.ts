jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITargetsystemcredentials, Targetsystemcredentials } from '../targetsystemcredentials.model';
import { TargetsystemcredentialsService } from '../service/targetsystemcredentials.service';

import { TargetsystemcredentialsRoutingResolveService } from './targetsystemcredentials-routing-resolve.service';

describe('Targetsystemcredentials routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TargetsystemcredentialsRoutingResolveService;
  let service: TargetsystemcredentialsService;
  let resultTargetsystemcredentials: ITargetsystemcredentials | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TargetsystemcredentialsRoutingResolveService);
    service = TestBed.inject(TargetsystemcredentialsService);
    resultTargetsystemcredentials = undefined;
  });

  describe('resolve', () => {
    it('should return ITargetsystemcredentials returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTargetsystemcredentials = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTargetsystemcredentials).toEqual({ id: 123 });
    });

    it('should return new ITargetsystemcredentials if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTargetsystemcredentials = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTargetsystemcredentials).toEqual(new Targetsystemcredentials());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Targetsystemcredentials })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTargetsystemcredentials = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTargetsystemcredentials).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
