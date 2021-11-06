jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISystemuser, Systemuser } from '../systemuser.model';
import { SystemuserService } from '../service/systemuser.service';

import { SystemuserRoutingResolveService } from './systemuser-routing-resolve.service';

describe('Systemuser routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SystemuserRoutingResolveService;
  let service: SystemuserService;
  let resultSystemuser: ISystemuser | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SystemuserRoutingResolveService);
    service = TestBed.inject(SystemuserService);
    resultSystemuser = undefined;
  });

  describe('resolve', () => {
    it('should return ISystemuser returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSystemuser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSystemuser).toEqual({ id: 123 });
    });

    it('should return new ISystemuser if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSystemuser = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSystemuser).toEqual(new Systemuser());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Systemuser })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSystemuser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSystemuser).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
