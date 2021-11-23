import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISystemuser, Systemuser } from '../systemuser.model';

import { SystemuserService } from './systemuser.service';

describe('Systemuser Service', () => {
  let service: SystemuserService;
  let httpMock: HttpTestingController;
  let elemDefault: ISystemuser;
  let expectedResult: ISystemuser | ISystemuser[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SystemuserService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      entryDate: currentDate,
      name: 'AAAAAAA',
      socialSecurityNumber: 'AAAAAAA',
      jobDescription: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          entryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Systemuser', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          entryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          entryDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Systemuser()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Systemuser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entryDate: currentDate.format(DATE_FORMAT),
          name: 'BBBBBB',
          socialSecurityNumber: 'BBBBBB',
          jobDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          entryDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Systemuser', () => {
      const patchObject = Object.assign(
        {
          entryDate: currentDate.format(DATE_FORMAT),
        },
        new Systemuser()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          entryDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Systemuser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entryDate: currentDate.format(DATE_FORMAT),
          name: 'BBBBBB',
          socialSecurityNumber: 'BBBBBB',
          jobDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          entryDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Systemuser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSystemuserToCollectionIfMissing', () => {
      it('should add a Systemuser to an empty array', () => {
        const systemuser: ISystemuser = { id: 123 };
        expectedResult = service.addSystemuserToCollectionIfMissing([], systemuser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemuser);
      });

      it('should not add a Systemuser to an array that contains it', () => {
        const systemuser: ISystemuser = { id: 123 };
        const systemuserCollection: ISystemuser[] = [
          {
            ...systemuser,
          },
          { id: 456 },
        ];
        expectedResult = service.addSystemuserToCollectionIfMissing(systemuserCollection, systemuser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Systemuser to an array that doesn't contain it", () => {
        const systemuser: ISystemuser = { id: 123 };
        const systemuserCollection: ISystemuser[] = [{ id: 456 }];
        expectedResult = service.addSystemuserToCollectionIfMissing(systemuserCollection, systemuser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemuser);
      });

      it('should add only unique Systemuser to an array', () => {
        const systemuserArray: ISystemuser[] = [{ id: 123 }, { id: 456 }, { id: 26340 }];
        const systemuserCollection: ISystemuser[] = [{ id: 123 }];
        expectedResult = service.addSystemuserToCollectionIfMissing(systemuserCollection, ...systemuserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const systemuser: ISystemuser = { id: 123 };
        const systemuser2: ISystemuser = { id: 456 };
        expectedResult = service.addSystemuserToCollectionIfMissing([], systemuser, systemuser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemuser);
        expect(expectedResult).toContain(systemuser2);
      });

      it('should accept null and undefined values', () => {
        const systemuser: ISystemuser = { id: 123 };
        expectedResult = service.addSystemuserToCollectionIfMissing([], null, systemuser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemuser);
      });

      it('should return initial array if no Systemuser is added', () => {
        const systemuserCollection: ISystemuser[] = [{ id: 123 }];
        expectedResult = service.addSystemuserToCollectionIfMissing(systemuserCollection, undefined, null);
        expect(expectedResult).toEqual(systemuserCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
