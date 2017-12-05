using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using FiscalDeTransitoApi.ViewModels;
using System.IO;
using FiscalDeTransitoApi.Context;
using System.Drawing;
using System.Drawing.Imaging;
using FiscalDeTransitoApi.Models;

namespace FiscalDeTransitoApi.Controllers
{
    [Produces("application/json")]
    [Route("api/Infracoes")]
    public class InfracoesController : Controller
    {
        private ApplicationDbContext DbContext { get; set; }

        public InfracoesController(ApplicationDbContext pDbContext)
        {
            DbContext = pDbContext;
        }

        [HttpPost]
        public async Task<IActionResult> UploadData(InfracaoViewModel pModel)
        {
            byte[] fileData = null;
            using (var memoryStream = new MemoryStream())
            {
                if (pModel.InfracaoFile != null)
                {
                    await pModel.InfracaoFile.CopyToAsync(memoryStream);
                    fileData = memoryStream.ToArray();
                }
            }
            var infracao = new Models.Infracao
            {
                Latitude = pModel.Latitude,
                Longitude = pModel.Longitude,
                IMEI = pModel.IMEI,
                Descricao = pModel.Descricao,
                DataRegistro = pModel.DataInfracao,
                File = new Models.InfracaoFile
                {
                    File = fileData
                }
            };
            DbContext.Infracoes.Add(infracao);
            /*var file = new Models.InfracaoFile
            {
                InfracaoId = infracao.Id,
                File = fileData
            };

            DbContext.Files.Add(file);*/

            DbContext.SaveChanges();
            pModel.Id = infracao.Id;
            pModel.InfracaoFile = null;

            return Ok(pModel);
        }

        [HttpDelete]
        public async Task<IActionResult> Delete(Guid InfracaoID)
        {
            var infracao = DbContext.Infracoes.Where(pX => pX.Id == InfracaoID).SingleOrDefault();
            DbContext.Infracoes.Remove(infracao);
            await DbContext.SaveChangesAsync();
            return Ok();
        }

        [HttpGet]
        public IActionResult Get()
        {
            var infracoes = DbContext.Infracoes.ToArray();
            return Ok(infracoes.Select(pX => new InfracaoViewModel
            {
                Id = pX.Id,
                Descricao = pX.Descricao,
                DataInfracao = pX.DataRegistro,
                IMEI = pX.IMEI,
                Latitude = pX.Latitude,
                Longitude = pX.Longitude
            }));

        }

        [HttpGet("{pData}")]
        public IActionResult Get(long pData)
        {
            var infracoes = DbContext.Infracoes.Where(pX => pX.IMEI == pData);
            return Ok(infracoes);
        }

        [HttpGet("image/{id}")]
        public IActionResult GetImage(Guid id)
        {
            var image = DbContext.Files.Where(pX => pX.InfracaoId == id).SingleOrDefault();
            if (image == null)
                return NotFound();
            return File(image.File, "image/png");
        }

        [HttpGet("thumbnail/{id}")]
        public IActionResult GetThumbnail(Guid id)
        {
            var imagem = DbContext.Files.Where(pX => pX.InfracaoId == id).SingleOrDefault();
            if (imagem != null && imagem.File != null)
                using (var memoryStream = new MemoryStream(imagem.File))
                {
                    var bitmap = System.Drawing.Bitmap.FromStream(memoryStream);
                    var thumnail = bitmap.GetThumbnailImage(64, 64, null, IntPtr.Zero);
                    using (var thumbStream = new MemoryStream())
                    {
                        thumnail.Save(thumbStream, ImageFormat.Png);
                        var data = thumbStream.ToArray();
                        return File(data, "image/png");
                    }
                }
            return NotFound();
        }

        [HttpPost("edit")]
        public IActionResult UpdateData(InfracaoViewModel value)
        {
            var infracao = DbContext.Infracoes.Find(value.Id);
            infracao.Descricao = value.Descricao;
            DbContext.SaveChanges();

            return Ok(value);
        }
    }
}