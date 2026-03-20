import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface FileMetrics {
  lineCount: number;
  functionCount: number;
  cyclomaticComplexity: number;
}

export interface FileAnalysisDetail {
  filePath: string;
  fileMetrics: FileMetrics;
}

export interface DirectoryAnalysisResult {
  totalLines: number;
  totalFunctions: number;
  totalComplexity: number;
  files: FileAnalysisDetail[];
}


@Injectable({
  providedIn: 'root'
})
export class Api {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  analyzeFile(file: File): Observable<FileMetrics> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<FileMetrics>(`${this.baseUrl}/analyze`, formData);
  }

  analyzeDirectory(path: string): Observable<DirectoryAnalysisResult> {
    return this.http.get<DirectoryAnalysisResult>(`${this.baseUrl}/analyze-directory?path=${encodeURIComponent(path)}`);
  }

  analyzeRepository(url: string): Observable<DirectoryAnalysisResult> {
    return this.http.get<DirectoryAnalysisResult>(`${this.baseUrl}/analyze-repo?url=${encodeURIComponent(url)}`);
  }
}
