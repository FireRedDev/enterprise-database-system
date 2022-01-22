import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITargetsystem, Targetsystem } from '../targetsystem.model';

import { TargetsystemService } from './targetsystem.service';

describe('Targetsystem Service', () => {
  let service: TargetsystemService;
  let httpMock: HttpTestingController;
  let elemDefault: ITargetsystem;
  let expectedResult: ITargetsystem | ITargetsystem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TargetsystemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      type: 'AAAAAAA',
      url: 'AAAAAAA',
      password: 'AAAAAAA',
      username: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Targetsystem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Targetsystem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Targetsystem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          type: 'BBBBBB',
          url: 'BBBBBB',
          password: 'BBBBBB',
          username: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Targetsystem', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          type: 'BBBBBB',
          url: 'BBBBBB',
        },
        new Targetsystem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Targetsystem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          type: 'BBBBBB',
          url: 'BBBBBB',
          password: 'BBBBBB',
          username: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Targetsystem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTargetsystemToCollectionIfMissing', () => {
      it('should add a Targetsystem to an empty array', () => {
        const targetsystem: ITargetsystem = { id: 123 };
        expectedResult = service.addTargetsystemToCollectionIfMissing([], targetsystem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(targetsystem);
      });

      it('should not add a Targetsystem to an array that contains it', () => {
        const targetsystem: ITargetsystem = { id: 123 };
        const targetsystemCollection: ITargetsystem[] = [
          {
            ...targetsystem,
          },
          { id: 456 },
        ];
        expectedResult = service.addTargetsystemToCollectionIfMissing(targetsystemCollection, targetsystem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Targetsystem to an array that doesn't contain it", () => {
        const targetsystem: ITargetsystem = { id: 123 };
        const targetsystemCollection: ITargetsystem[] = [{ id: 456 }];
        expectedResult = service.addTargetsystemToCollectionIfMissing(targetsystemCollection, targetsystem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(targetsystem);
      });

      it('should add only unique Targetsystem to an array', () => {
        const targetsystemArray: ITargetsystem[] = [{ id: 123 }, { id: 456 }, { id: 48230 }];
        const targetsystemCollection: ITargetsystem[] = [{ id: 123 }];
        expectedResult = service.addTargetsystemToCollectionIfMissing(targetsystemCollection, ...targetsystemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const targetsystem: ITargetsystem = { id: 123 };
        const targetsystem2: ITargetsystem = { id: 456 };
        expectedResult = service.addTargetsystemToCollectionIfMissing([], targetsystem, targetsystem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(targetsystem);
        expect(expectedResult).toContain(targetsystem2);
      });

      it('should accept null and undefined values', () => {
        const targetsystem: ITargetsystem = { id: 123 };
        expectedResult = service.addTargetsystemToCollectionIfMissing([], null, targetsystem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(targetsystem);
      });

      it('should return initial array if no Targetsystem is added', () => {
        const targetsystemCollection: ITargetsystem[] = [{ id: 123 }];
        expectedResult = service.addTargetsystemToCollectionIfMissing(targetsystemCollection, undefined, null);
        expect(expectedResult).toEqual(targetsystemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
