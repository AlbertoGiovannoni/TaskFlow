import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.FieldPackage.Document;

@Service
public class DocumentService {

    @Autowired
    private FieldDAO fieldDao;

    public Document savePdf(MultipartFile file) throws Exception {
        Document pdfDocument = new Document();
        pdfDocument.setFileName(file.getOriginalFilename());
        pdfDocument.setContentType(file.getContentType());
        pdfDocument.setContent(file.getBytes());

        return fieldDao.save(pdfDocument);
    }
    public Document getDocumentById(String id) {
        Document document = (Document)this.fieldDao.findById(id).orElseThrow();
        return document;
    }
}